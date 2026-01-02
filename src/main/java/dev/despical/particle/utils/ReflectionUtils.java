/*
 * ParticleLib - A library for managing particles
 * Copyright (C) 2026  Berke Akçen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package dev.despical.particle.utils;

import dev.despical.particle.ParticleConstants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static dev.despical.particle.ParticleConstants.BLOCK_POSITION_CONSTRUCTOR;

/**
 * @author ByteZ
 * @see ParticleConstants
 * @since 30.08.2018
 */
public final class ReflectionUtils {

	/* ---------------- NMS & CB paths ---------------- */

	/**
	 * The current Minecraft version as an int.
	 */
	public static final double MINECRAFT_VERSION; // TODO switch to version object
	/**
	 * Represents the net minecraft server path
	 * <p>
	 * e.g. {@code net.minecraft.server.v1_8_R3}, {@code net.minecraft.server.v1_12_R1}
	 */
	private static final String NET_MINECRAFT_SERVER_PACKAGE_PATH;
	/**
	 * Represents the craftbukkit path
	 * <p>
	 * e.g. {@code org.bukkit.craftbukkit.v1_8_R3}, {@code org.bukkit.craftbukkit.v1_12_R1}
	 */
	private static final String CRAFT_BUKKIT_PACKAGE_PATH;
	/**
	 * ZipFile containing ParticleLib.
	 */
	private static final ZipFile zipFile;

	static {
		String serverPath = Bukkit.getServer().getClass().getPackage().getName();
		String version = serverPath.substring(serverPath.lastIndexOf(".") + 1);
		String bukkitVersion = Bukkit.getBukkitVersion();
		int dashIndex = bukkitVersion.indexOf("-");
		MINECRAFT_VERSION = Double.parseDouble(bukkitVersion.substring(2, dashIndex > -1 ? bukkitVersion.indexOf("-") : bukkitVersion.length()));
		NET_MINECRAFT_SERVER_PACKAGE_PATH = "net.minecraft" + (MINECRAFT_VERSION < 17 ? ".server." + version : "");
		CRAFT_BUKKIT_PACKAGE_PATH = "org.bukkit.craftbukkit." + version;
		try {
			zipFile = new ZipFile(ReflectionUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (IOException | URISyntaxException ex) {
			throw new IllegalStateException("Error while finding zip file", ex);
		}
	}

	/**
	 * Gets a class but returns null instead of throwing
	 * a {@link ClassNotFoundException}.
	 *
	 * @param path the path of the class
	 * @return the class. If the class isn't found null
	 */
	public static Class<?> getClassSafe(String path) {
		try {
			return Class.forName(path);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Gets the nms path of a class without depending on versions
	 * <p>
	 * e.g.
	 * getNMSPath("Block")  = "net.minecraft.server.v1_14_R1.Block"
	 * getNMSPath("Entity") = "net.minecraft.server.v1_12_R1.Entity"
	 *
	 * @param path the path that should be added
	 *             to the nms path
	 * @return the nms path
	 */
	public static String getNMSPath(String path) {
		return getNetMinecraftServerPackagePath() + "." + path;
	}

	/**
	 * Directly gets the class object over the path
	 *
	 * @param path the path of the class
	 * @return the class. If the class isn't found null
	 */
	public static Class<?> getNMSClass(String path) {
		return getClassSafe(getNMSPath(path));
	}

	/**
	 * Gets the craftbukkit path of a class without depending on versions
	 * <p>
	 * e.g.
	 * getCraftBukkitPath("CraftChunk")              = "org.bukkit.craftbukkit.v1_15_R1.CraftChunk"
	 * getCraftBukkitPath("event.CraftEventFactory") = "org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory"
	 *
	 * @param path the path that should be added to the craftbukkit path
	 * @return the craftbukkit path
	 */
	public static String getCraftBukkitPath(String path) {
		return getCraftBukkitPackagePath() + "." + path;
	}

	/**
	 * Method to directly get the class object over the path
	 *
	 * @param path the path of the class
	 * @return the class. If the class isn't found null
	 */
	public static Class<?> getCraftBukkitClass(String path) {
		return getClassSafe(getCraftBukkitPath(path));
	}

	/**
	 * Method to not get disturbed by the forced try catch block
	 *
	 * @param targetClass    the {@link Class} the {@link Method} is in
	 * @param methodName     the name of the target {@link Method}
	 * @param parameterTypes the parameterTypes of the {@link Method}
	 * @return if found the target {@link Method}. If not found null.
	 */
	public static Method getMethodOrNull(Class<?> targetClass, String methodName, Class<?>... parameterTypes) {
		try {
			return targetClass.getMethod(methodName, parameterTypes);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Method to not get disturbed by the forced try catch block
	 *
	 * @param targetClass the {@link Class} the {@link Field} is in
	 * @param fieldName   the name of the target {@link Field}
	 * @param declared    defines if the target {@link Field} is private
	 * @return if found the target {@link Field}. If not found null.
	 */
	public static Field getFieldOrNull(Class<?> targetClass, String fieldName, boolean declared) {
		try {
			return declared ? targetClass.getDeclaredField(fieldName) : targetClass.getField(fieldName);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Gets a constructor without throwing exceptions
	 *
	 * @param targetClass    the {@link Class} the {@link Constructor} is in
	 * @param parameterTypes the parameterTypes of the {@link Constructor}
	 * @return if found the target {@link Constructor}. If not found null.
	 */
	public static Constructor<?> getConstructorOrNull(Class<?> targetClass, Class<?>... parameterTypes) {
		try {
			return targetClass.getConstructor(parameterTypes);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Reads the specified {@link Field} from the specified {@link Object}. When the
	 * {@link Field} is static set the object to {@code null}.
	 *
	 * @param field  the {@link Field} from which the value should be extracted.
	 * @param object the {@link Object} from which the specified {@link Field Fields} value is to be extracted.
	 * @return the extracted value of the specified {@link Field} in the specified {@link Object}.
	 */
	public static <T> T readField(Field field, Object object) {
		if (field == null)
			return null;
		try {
			return (T) field.get(object);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * @return the nms path
	 */
	public static String getNetMinecraftServerPackagePath() {
		return NET_MINECRAFT_SERVER_PACKAGE_PATH;
	}

	/**
	 * @return the craftbukkit path
	 */
	public static String getCraftBukkitPackagePath() {
		return CRAFT_BUKKIT_PACKAGE_PATH;
	}

	/**
	 * Creates a new MinecraftKey with the given data.
	 *
	 * @param key the data that should be
	 *            used in the constructor
	 *            of the key.
	 * @return the new MinecraftKey
	 */
	public static Object getMinecraftKey(String key) {
		if (key == null)
			return null;
		try {
			return ParticleConstants.MINECRAFT_KEY_CONSTRUCTOR.newInstance(key);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Creates a new Vector3fa instance.
	 *
	 * @param x x value of the vector.
	 * @param y y value of the vector.
	 * @param z z value of the vector.
	 * @return a Vector3fa instance with the specified coordinates.
	 */
	public static Object createVector3fa(float x, float y, float z) {
		try {
			return ParticleConstants.VECTOR_3FA_CONSTRUCTOR.newInstance(x, y, z);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Creates a new BlockPosition.
	 *
	 * @param location the {@link Location} of the block.
	 * @return the BlockPosition of the location
	 */
	public static Object createBlockPosition(Location location) {
		try {
			return BLOCK_POSITION_CONSTRUCTOR.newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Gets the Entity instance of a CraftEntity
	 *
	 * @param entity the CraftEntity
	 * @return the Entity instance of the defined CraftEntity or {@code null} if either the given parameter is invalid or an error occurs.
	 */
	public static Object getEntityHandle(Entity entity) {
		if (entity == null || !ParticleConstants.CRAFT_ENTITY_CLASS.isAssignableFrom(entity.getClass()))
			return null;
		try {
			return ParticleConstants.CRAFT_ENTITY_GET_HANDLE_METHOD.invoke(entity);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Gets the {@link InputStream} of a resource.
	 *
	 * @param resource the name of the resource
	 * @return the {@link InputStream} of the resource
	 */
	public static InputStream getResourceStreamSafe(String resource) {
		ZipEntry entry = zipFile.getEntry(resource);
		if (entry == null)
			return null;
		try {
			return zipFile.getInputStream(entry);
		} catch (IOException ex) {
			return null;
		}
	}

}
