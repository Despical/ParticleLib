/*
 * KOTL - Don't let others climb to top of the ladders!
 * Copyright (C) 2024  Berke Akçen
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

package me.despical.particle.utils;

import me.despical.particle.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Despical
 * <p>
 * Created at 6.04.2024
 */
public final class NMSUtils {

	private NMSUtils() {
	}

	private static Particle toBukkit(ParticleEffect effect) {
		return Particle.valueOf(effect.getFieldName().toUpperCase(Locale.ENGLISH));
	}

	/**
	 * Displays the given particle to all players.
	 */
	public static void display(Object packet, ParticleEffect effect, Location location, int amount) {
		List<Player> players = Objects.requireNonNull(location.getWorld()).getPlayers();

		if (me.despical.particle.ParticleEffect.USE_API) {
			players.forEach(player -> player.spawnParticle(toBukkit(effect), location, amount));
			return;
		}

		players.forEach(p -> PacketUtils.sendPacket(p, packet));
	}

	/**
	 * Displays the given particle to the players in the array.
	 *
	 * @param players The players that should see the particle.
	 */
	public static void display(Object packet, ParticleEffect effect, Location location, int amount, Player... players) {
		if (me.despical.particle.ParticleEffect.USE_API) {
			Arrays.asList(players).forEach(player -> player.spawnParticle(toBukkit(effect), location, amount));
			return;
		}

		display(packet, effect, location, amount, Arrays.asList(players));
	}

	/**
	 * Display the given particle to online player that match the given filter.
	 *
	 * @param filter a {@link Predicate} to filter out
	 *               specific {@link Player Players}.
	 */
	public static void display(Object packet, ParticleEffect effect, Location location, int amount, Predicate<Player> filter) {
		if (me.despical.particle.ParticleEffect.USE_API) {
			Bukkit.getOnlinePlayers().stream().filter(filter).forEach(player -> player.spawnParticle(toBukkit(effect), location, amount));
			return;
		}

		Bukkit.getOnlinePlayers()
			.stream()
			.filter(p -> filter.test(p) && p.getWorld().equals(location.getWorld()))
			.forEach(p -> PacketUtils.sendPacket(p, packet));
	}

	/**
	 * Displays the given particle to all players in the {@link Collection}
	 *
	 * @param players a list of players that should receive the particle packet.
	 */
	public static void display(Object packet, ParticleEffect effect, Location location, int amount, Collection<? extends Player> players) {
		if (ParticleEffect.USE_API) {
			players.forEach(player -> player.spawnParticle(toBukkit(effect), location, amount));
			return;
		}

		players.stream()
			.filter(p -> p.getWorld().equals(location.getWorld()))
			.forEach(p -> PacketUtils.sendPacket(p, packet));
	}
}