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

package me.despical.particle.data.color;

import me.despical.particle.PropertyType;
import me.despical.particle.data.ParticleData;

/**
 * The {@link ParticleColor} class is used to store the rgb values of colors and
 * convert them into the corresponding nms objects.
 *
 * @author ByteZ
 * @see PropertyType#COLORABLE
 * @since 10.06.2019
 */
public abstract class ParticleColor extends ParticleData {
    
    /**
     * The red value of the rgb value.
     */
    private final int red;
    /**
     * The green value of the rgb value.
     */
    private final int green;
    /**
     * The blue value of the rgb value.
     */
    private final int blue;
    
    /**
     * Initializes a new {@link ParticleData} object.
     *
     * @param red   the red value of the color.
     * @param green the green value of the color.
     * @param blue  the blue value of the color.
     */
    ParticleColor(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    /**
     * Converts the current {@link ParticleData} instance into nms data. If the current
     * minecraft version was released before 1.13 an int array should be returned. If the
     * version was released after 1.12 a nms "ParticleParam" has to be returned.
     *
     * @return the nms data.
     */
    @Override
    public abstract Object toNMSData();
    
    /**
     * Gets the red value of the color.
     *
     * @return the red value.
     */
    public float getRed() {
        return red;
    }
    
    /**
     * Gets green red value of the color.
     *
     * @return the green value.
     */
    public float getGreen() {
        return green;
    }
    
    /**
     * Gets the blue value of the color.
     *
     * @return the blue value.
     */
    public float getBlue() {
        return blue;
    }
}
