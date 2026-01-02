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

import dev.despical.particle.data.ParticleData;

import java.util.Random;

/**
 * Utility for Maths
 *
 * @author ByteZ
 * @since 14.09.2018
 */
public final class MathUtils {

	/**
	 * An easy to access {@link Random} implementation for random number
	 * generation. This specific field is mostly used by the random
	 * methods of the {@link ParticleData} types.
	 */
	public static final Random RANDOM = new Random();

	/**
	 * Generates a random {@link Integer}.
	 *
	 * @param minimum the minimum value of the generated value.
	 * @param maximum the maximum value of the generated value.
	 * @return a randomly generated {@link Integer} in the defined range.
	 * @see #RANDOM
	 */
	public static int generateRandomInteger(int minimum, int maximum) {
		return minimum + (int) (RANDOM.nextDouble() * ((maximum - minimum) + 1));
	}

	/**
	 * Checks if a specific {@link Integer} is in the given range.
	 * If not the respective bound of the range is returned.
	 *
	 * @param value the value which should be checked.
	 * @param max   the maximum value.
	 * @param min   the minimum value
	 * @return the calculated value.
	 */
	public static int getMaxOrMin(int value, int max, int min) {
		return value < max ? (Math.max(value, min)) : max;
	}
}
