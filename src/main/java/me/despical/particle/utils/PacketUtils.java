package me.despical.particle.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Despical
 * <p>
 * Created at 4.04.2024
 */
public final class PacketUtils {

	/**
	 * We use reflection mainly to avoid writing a new class for version barrier.
	 * The version barrier is for NMS that uses the Minecraft version as the main package name.
	 * <p>
	 * E.g. EntityPlayer in 1.15 is in the class {@code net.minecraft.server.v1_15_R1}
	 * but in 1.14 it's in {@code net.minecraft.server.v1_14_R1}
	 * In order to maintain cross-version compatibility we cannot import these classes.
	 * <p>
	 * Performance is not a concern for these specific statically initialized values.
	 * <p>
	 * <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-legacy/">Versions Legacy</a>
	 */
	public static final String NMS_VERSION;

	static { // This needs to be right below VERSION because of initialization order.
		// This package loop is used to avoid implementation-dependant strings like Bukkit.getVersion() or Bukkit.getBukkitVersion()
		// which allows easier testing as well.
		String found = null;
		for (Package pack : Package.getPackages()) {
			String name = pack.getName();

			// .v because there are other packages.
			if (name.startsWith("org.bukkit.craftbukkit.v")) {
				found = pack.getName().split("\\.")[3];

				// Just a final guard to make sure it finds this important class.
				// As a protection for forge+bukkit implementation that tend to mix versions.
				// The real CraftPlayer should exist in the package.
				// Note: Doesn't seem to function properly. Will need to separate the version
				// handler for NMS and CraftBukkit for softwares like catmc.
				try {
					Class.forName("org.bukkit.craftbukkit." + found + ".entity.CraftPlayer");
					break;
				} catch (ClassNotFoundException e) {
					found = null;
				}
			}
		}
		if (found == null)
			throw new IllegalArgumentException("Failed to parse server version. Could not find any package starting with name: 'org.bukkit.craftbukkit.v'");
		NMS_VERSION = found;
	}

	public static final int MINOR_NUMBER, PATCH_NUMBER;

	static {
		String[] split = NMS_VERSION.substring(1).split("_");
		if (split.length < 1) {
			throw new IllegalStateException("Version number division error: " + Arrays.toString(split) + ' ' + getVersionInformation());
		}

		String minorVer = split[1];
		try {
			MINOR_NUMBER = Integer.parseInt(minorVer);
			if (MINOR_NUMBER < 0)
				throw new IllegalStateException("Negative minor number? " + minorVer + ' ' + getVersionInformation());
		} catch (Throwable ex) {
			throw new RuntimeException("Failed to parse minor number: " + minorVer + ' ' + getVersionInformation(), ex);
		}

		// Bukkit.getBukkitVersion() = "1.12.2-R0.1-SNAPSHOT"
		Matcher bukkitVer = Pattern.compile("^\\d+\\.\\d+\\.(\\d+)").matcher(Bukkit.getBukkitVersion());
		if (bukkitVer.find()) { // matches() won't work, we just want to match the start using "^"
			try {
				// group(0) gives the whole matched string, we just want the captured group.
				PATCH_NUMBER = Integer.parseInt(bukkitVer.group(1));
			} catch (Throwable ex) {
				throw new RuntimeException("Failed to parse minor number: " + bukkitVer + ' ' + getVersionInformation(), ex);
			}
		} else {
			// 1.8-R0.1-SNAPSHOT
			PATCH_NUMBER = 0;
		}
	}

	public static String getVersionInformation() {
		return "(NMS: " + NMS_VERSION + " | " +
				"Minecraft: " + Bukkit.getVersion() + " | " +
				"Bukkit: " + Bukkit.getBukkitVersion() + ')';
	}

	public static final String
			CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit." + NMS_VERSION + '.',
			NMS_PACKAGE = v(17, "net.minecraft.").orElse("net.minecraft.server." + NMS_VERSION + '.');
	/**
	 * A nullable public accessible field only available in {@code EntityPlayer}.
	 * This can be null if the player is offline.
	 */
	private static final MethodHandle PLAYER_CONNECTION;
	/**
	 * Responsible for getting the NMS handler {@code EntityPlayer} object for the player.
	 * {@code CraftPlayer} is simply a wrapper for {@code EntityPlayer}.
	 * Used mainly for handling packet related operations.
	 * <p>
	 * This is also where the famous player {@code ping} field comes from!
	 */
	private static final MethodHandle GET_HANDLE;
	/**
	 * Sends a packet to the player's client through a {@code NetworkManager} which
	 * is where {@code ProtocolLib} controls packets by injecting channels!
	 */
	private static final MethodHandle SEND_PACKET;

	static {
		Class<?> entityPlayer = getNMSClass("server.level", "EntityPlayer");
		Class<?> craftPlayer = getCraftClass("entity.CraftPlayer");
		Class<?> playerConnection = getNMSClass("server.network", "PlayerConnection");
		Class<?> playerCommonConnection;
		if (supports(20) && supportsPatch(2)) {
			// The packet send method has been abstracted from ServerGamePacketListenerImpl to ServerCommonPacketListenerImpl in 1.20.2
			playerCommonConnection = getNMSClass("server.network", "ServerCommonPacketListenerImpl");
		} else {
			playerCommonConnection = playerConnection;
		}

		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle sendPacket = null, getHandle = null, connection = null;

		try {
			connection = lookup.findGetter(entityPlayer,
					v(20, "c").v(17, "b").orElse("playerConnection"), playerConnection);
			getHandle = lookup.findVirtual(craftPlayer, "getHandle", MethodType.methodType(entityPlayer));
			sendPacket = lookup.findVirtual(playerCommonConnection,
					v(20, 2, "b").v(18, "a").orElse("sendPacket"),
					MethodType.methodType(void.class, getNMSClass("network.protocol", "Packet")));
		} catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException ex) {
			ex.printStackTrace();
		}

		PLAYER_CONNECTION = connection;
		SEND_PACKET = sendPacket;
		GET_HANDLE = getHandle;
	}

	private PacketUtils() {
	}

	public static <T> VersionHandler<T> v(int version, T handle) {
		return new VersionHandler<>(version, handle);
	}

	/**
	 * Overload for {@link #v(int, T)} that supports patch versions
	 *
	 * @since 9.5.0
	 */
	public static <T> VersionHandler<T> v(int version, int patch, T handle) {
		return new VersionHandler<>(version, patch, handle);
	}

	/**
	 * Checks whether the server version is equal or greater than the given version.
	 *
	 * @param minorNumber the version to compare the server version with.
	 * @return true if the version is equal or newer, otherwise false.
	 * @see #MINOR_NUMBER
	 * @since 4.0.0
	 */
	public static boolean supports(int minorNumber) {
		return MINOR_NUMBER >= minorNumber;
	}

	/**
	 * Checks whether the server version is equal or greater than the given version.
	 *
	 * @param patchNumber the version to compare the server version with.
	 * @return true if the version is equal or newer, otherwise false.
	 * @see #PATCH_NUMBER
	 * @since 7.0.0
	 */
	public static boolean supportsPatch(int patchNumber) {
		return PATCH_NUMBER >= patchNumber;
	}

	/**
	 * Get a NMS (net.minecraft.server) class which accepts a package for 1.17 compatibility.
	 *
	 * @param packageName the 1.17+ package name of this class.
	 * @param name        the name of the class.
	 * @return the NMS class or null if not found.
	 * @since 4.0.0
	 */
	public static Class<?> getNMSClass(@Nullable String packageName, @Nonnull String name) {
		if (packageName != null && supports(17)) name = packageName + '.' + name;

		try {
			return Class.forName(NMS_PACKAGE + name);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Sends a packet to the player asynchronously if they're online.
	 * Packets are thread-safe.
	 *
	 * @param player  the player to send the packet to.
	 * @param packets the packets to send.
	 * @return the async thread handling the packet.
	 * @see #sendPacketSync(Player, Object...)
	 * @since 1.0.0
	 */
	@Nonnull
	public static CompletableFuture<Void> sendPacket(@Nonnull Player player, @Nonnull Object... packets) {
		return CompletableFuture.runAsync(() -> sendPacketSync(player, packets))
				.exceptionally(ex -> {
					ex.printStackTrace();
					return null;
				});
	}

	/**
	 * Sends a packet to the player synchronously if they're online.
	 *
	 * @param player  the player to send the packet to.
	 * @param packets the packets to send.
	 * @see #sendPacket(Player, Object...)
	 * @since 2.0.0
	 */
	public static void sendPacketSync(@Nonnull Player player, @Nonnull Object... packets) {
		try {
			Object handle = GET_HANDLE.invoke(player);
			Object connection = PLAYER_CONNECTION.invoke(handle);

			// Checking if the connection is not null is enough. There is no need to check if the player is online.
			if (connection != null) {
				for (Object packet : packets) SEND_PACKET.invoke(connection, packet);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	/**
	 * Get a CraftBukkit (org.bukkit.craftbukkit) class.
	 *
	 * @param name the name of the class to load.
	 * @return the CraftBukkit class or null if not found.
	 * @since 1.0.0
	 */
	public static Class<?> getCraftClass(@Nonnull String name) {
		try {
			return Class.forName(CRAFTBUKKIT_PACKAGE + name);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static final class VersionHandler<T> {

		private int version, patch;
		private T handle;

		private VersionHandler(int version, T handle) {
			this(version, 0, handle);
		}

		private VersionHandler(int version, int patch, T handle) {
			if (supports(version) && supportsPatch(patch)) {
				this.version = version;
				this.patch = patch;
				this.handle = handle;
			}
		}

		public VersionHandler<T> v(int version, T handle) {
			return v(version, 0, handle);
		}

		public VersionHandler<T> v(int version, int patch, T handle) {
			if (version == this.version && patch == this.patch)
				throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + version + '.' + patch);
			if (version > this.version && supports(version) && patch >= this.patch && supportsPatch(patch)) {
				this.version = version;
				this.patch = patch;
				this.handle = handle;
			}
			return this;
		}

		/**
		 * If none of the previous version checks matched, it'll return this object.
		 */
		public T orElse(T handle) {
			return this.version == 0 ? handle : this.handle;
		}
	}
}