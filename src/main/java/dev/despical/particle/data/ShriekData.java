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

/**
 * This class holds the delay data needed, to display the {@link ParticleEffect#SHRIEK} particle. The delay is in ticks.
 * The client will wait this amount of ticks before displaying the particle.
 *
 * @author ByteZ
 * @see ParticleEffect#SHRIEK
 */
public final class ShriekData extends ParticleData {

	/**
	 * The delay in ticks.
	 */
	private final int delay;

	/**
	 * Constructs a new {@link ShriekData} instance.
	 *
	 * @param delay The delay in ticks.
	 */
	public ShriekData(int delay) {
		this.delay = delay;
	}

	/**
	 * Gets the delay in ticks.
	 *
	 * @return The delay in ticks.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Creates a new ShriekParticleOption instance with the data of the current {@link ShriekData} instance.
	 * <p>
	 * Please note that this class is not supported in any versions before 1.19 and could lead to errors
	 * if used in legacy versions.
	 *
	 * @return a new ShriekParticleOption instance with the data of the current {@link ShriekData} instance.
	 */
	@Override
	public Object toNMSData() {
		if (ReflectionUtils.MINECRAFT_VERSION < 19 || getEffect() != ParticleEffect.SHRIEK)
			return null;
		try {
			return ParticleConstants.PARTICLE_PARAM_SHRIEK_CONSTRUCTOR.newInstance(getDelay());
		} catch (Exception ex) {
			return null;
		}
	}
}
