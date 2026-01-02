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
 * This class holds the roll data needed, to display the {@link ParticleEffect#SCULK_CHARGE} particle. This roll data is
 * a float ranging from 0 to 1.0. The particle will roll from 0 to 360 degrees.
 *
 * @author ByteZ
 * @see ParticleEffect#SCULK_CHARGE
 */
public final class SculkChargeData extends ParticleData {

	/**
	 * The roll data.
	 */
	private final float roll;

	/**
	 * Constructs a new {@link SculkChargeData} instance.
	 *
	 * @param roll The roll data.
	 */
	public SculkChargeData(float roll) {
		this.roll = roll;
	}

	/**
	 * Gets the roll data.
	 *
	 * @return The roll data.
	 */
	public float getRoll() {
		return roll;
	}

	/**
	 * Creates a new SculkChargeParticleOptions instance with the data of the current {@link SculkChargeData} instance.
	 * <p>
	 * Please note that this class is not supported in any versions before 1.19 and could lead to errors
	 * if used in legacy versions.
	 *
	 * @return a new SculkChargeParticleOptions instance with the data of the current {@link SculkChargeData} instance.
	 */
	@Override
	public Object toNMSData() {
		if (ReflectionUtils.MINECRAFT_VERSION < 19 || getEffect() != ParticleEffect.SCULK_CHARGE)
			return null;
		try {
			return ParticleConstants.PARTICLE_PARAM_SCULK_CHARGE_CONSTRUCTOR.newInstance(getRoll());
		} catch (Exception ex) {
			return null;
		}
	}
}
