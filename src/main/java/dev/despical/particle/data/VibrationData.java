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

package dev.despical.particle.data;

import dev.despical.particle.ParticleConstants;
import dev.despical.particle.ParticleEffect;
import dev.despical.particle.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Objects;

/**
 * This class holds all data that is needed by the client to display a {@link ParticleEffect#VIBRATION}
 * particle. The required information is: The start {@link Location} (<b>Only for pre 1.19 version</b>), The destination
 * and the amount of ticks it will take the particle to fly this path.
 * <p>
 * Minecraft only supports full block coordinates for the start and destination location. So any
 * particle will spawn at the center of a block. (<b>Again, only for pre 1.19 versions. The client uses the normal spawn
 * location of the particle since 1.19</b>)
 *
 * @author ByteZ
 * @see ParticleEffect#VIBRATION
 */
public final class VibrationData extends ParticleData {

	/**
	 * The start {@link Location} of the particle. (Will be mapped to the block location)
	 */
	private final Location start;
	/**
	 * The destination {@link Location} of the particle. (Will be mapped to the block location)
	 */
	private final Location blockDestination;
	/**
	 * The destination {@link Entity} of the particle.
	 */
	private final Entity entitydestination;
	/**
	 * The amount of ticks it will take the particle to fly from the {@link #start} to the destination
	 */
	private final int ticks;

	/**
	 * Creates a new {@link VibrationData} instance.
	 *
	 * @param start       the start {@link Location} of the particle. (<b>Not needed since 1.19</b>)
	 * @param destination the destination {@link Location} of the particle.
	 * @param ticks       the amount of ticks it will take the particle to reach the {@link #blockDestination}
	 */
	public VibrationData(Location start, Location destination, int ticks) {
		this.start = Objects.requireNonNull(start);
		this.blockDestination = Objects.requireNonNull(destination);
		this.entitydestination = null;
		this.ticks = ticks;
	}

	/**
	 * Creates a new {@link VibrationData} instance.
	 *
	 * @param destination the destination {@link Location} of the particle.
	 * @param ticks       the amount of ticks it will take the particle to reach the {@link #blockDestination}
	 */
	public VibrationData(Location destination, int ticks) {
		this.start = null;
		this.blockDestination = Objects.requireNonNull(destination);
		this.entitydestination = null;
		this.ticks = ticks;
	}

	/**
	 * Creates a new {@link VibrationData} instance.
	 *
	 * @param start       the start {@link Location} of the particle. (<b>Not needed since 1.19</b>)
	 * @param destination the destination {@link Entity} of the particle.
	 * @param ticks       the amount of ticks it will take the particle to reach the {@link #blockDestination}
	 */
	public VibrationData(Location start, Entity destination, int ticks) {
		this.start = Objects.requireNonNull(start);
		this.entitydestination = Objects.requireNonNull(destination);
		this.blockDestination = null;
		this.ticks = ticks;
	}

	/**
	 * Creates a new {@link VibrationData} instance.
	 *
	 * @param destination the destination {@link Entity} of the particle.
	 * @param ticks       the amount of ticks it will take the particle to reach the {@link #blockDestination}
	 */
	public VibrationData(Entity destination, int ticks) {
		this.start = null;
		this.entitydestination = Objects.requireNonNull(destination);
		this.blockDestination = null;
		this.ticks = ticks;
	}

	/**
	 * Gets the start {@link Location} of the particle.
	 *
	 * @return the start {@link Location} of the particle.
	 */
	public Location getStart() {
		return start;
	}

	/**
	 * Gets the destination {@link Location} of the particle.
	 *
	 * @return the destination {@link Location} of the particle.
	 */
	public Location getBlockDestination() {
		return blockDestination;
	}

	/**
	 * Gets the destination {@link Entity} of the particle.
	 *
	 * @return the destination {@link Entity} of the particle.
	 */
	public Entity getEntityDestination() {
		return entitydestination;
	}

	/**
	 * Gets the amount of ticks it will take the particle to travel.
	 *
	 * @return the travel time in ticks.
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Creates a new VibrationParticleOption instance with the data of
	 * the current {@link VibrationData} instance.
	 * <p>
	 * Please note that this class is not supported in
	 * any versions before 1.17 and could lead to errors
	 * if used in legacy versions.
	 *
	 * @return a new VibrationParticleOption with the data of the current {@link VibrationData} instance.
	 */
	@Override
	public Object toNMSData() {
		if (ReflectionUtils.MINECRAFT_VERSION < 17 || getEffect() != ParticleEffect.VIBRATION)
			return null;
		boolean isBlockDest = blockDestination != null;
		Object start = ReflectionUtils.createBlockPosition(getStart());
		try {
			if (ReflectionUtils.MINECRAFT_VERSION < 19) {
				Object source;
				if (isBlockDest) {
					Object dest = ReflectionUtils.createBlockPosition(getBlockDestination());
					source = ParticleConstants.BLOCK_POSITION_SOURCE_CONSTRUCTOR.newInstance(dest);
				} else
					source = ParticleConstants.ENTITY_POSITION_SOURCE_CONSTRUCTOR.newInstance(getEntityDestination().getEntityId());
				Object path = ParticleConstants.VIBRATION_PATH_CONSTRUCTOR.newInstance(start, source, getTicks());
				return ParticleConstants.PARTICLE_PARAM_VIBRATION_CONSTRUCTOR.newInstance(path);
			} else {
				Object source;
				if (isBlockDest) {
					Object dest = ReflectionUtils.createBlockPosition(getBlockDestination());
					source = ParticleConstants.BLOCK_POSITION_SOURCE_CONSTRUCTOR.newInstance(dest);
				} else
					source = ParticleConstants.ENTITY_POSITION_SOURCE_CONSTRUCTOR.newInstance(ReflectionUtils.getEntityHandle(getEntityDestination()), 0f);
				return ParticleConstants.PARTICLE_PARAM_VIBRATION_CONSTRUCTOR.newInstance(source, getTicks());
			}
		} catch (Exception ex) {
			return null;
		}
	}
}
