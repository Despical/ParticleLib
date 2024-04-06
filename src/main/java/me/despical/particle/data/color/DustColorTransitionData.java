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

import me.despical.particle.utils.ReflectionUtils;
import me.despical.particle.ParticleConstants;
import me.despical.particle.ParticleEffect;
import me.despical.particle.PropertyType;

import java.awt.*;

/**
 * This {@link me.despical.particle.data.color.DustData} implementation is solely built
 * for the {@link ParticleEffect#DUST_COLOR_TRANSITION}
 * effect. It supports 2 colors and a custom size. The
 * resulting particle will show a color fade between
 * those 2 colors.
 *
 * @author ByteZ
 * @see me.despical.particle.data.color.DustData
 * @see PropertyType#DUST
 * @see ParticleEffect#DUST_COLOR_TRANSITION
 */
public final class DustColorTransitionData extends DustData {
    
    /**
     * The red value of the second color.
     */
    private final int fadeRed;
    /**
     * The green value of the second color.
     */
    private final int fadeGreen;
    /**
     * The blue value of the second color.
     */
    private final int fadeBlue;
    
    /**
     * Creates a new {@link DustColorTransitionData} instance.
     *
     * @param color     the start color of the particle.
     * @param fadeColor the color the particle will fade to.
     * @param size      the size of the particle.
     */
    public DustColorTransitionData(Color color, Color fadeColor, float size) {
        super(color, size);
        fadeRed = fadeColor.getRed();
        fadeGreen = fadeColor.getGreen();
        fadeBlue = fadeColor.getBlue();
    }
    
    /**
     * Creates a new {@link DustColorTransitionData} instance.
     *
     * @param red       the red value of the start color.
     * @param green     the green value of the start color.
     * @param blue      the blue value of the start color.
     * @param fadeRed   the red value of the second color.
     * @param fadeGreen the green value of the second color.
     * @param fadeBlue  the blue value of the second color.
     * @param size      the size of the particle.
     */
    public DustColorTransitionData(int red, int green, int blue, int fadeRed, int fadeGreen, int fadeBlue, float size) {
        super(red, green, blue, size);
        this.fadeRed = fadeRed;
        this.fadeGreen = fadeGreen;
        this.fadeBlue = fadeBlue;
    }
    
    /**
     * Gets the red value of the color the particle will
     * fade to. <b>(Value range is 0f-1f)</b>
     *
     * @return the red value of the second color.
     */
    public float getFadeRed() {
        return fadeRed / 255f;
    }
    
    /**
     * Gets the green value of the color the particle will
     * fade to. <b>(Value range is 0f-1f)</b>
     *
     * @return the red value of the second color.
     */
    public float getFadeGreen() {
        return fadeGreen / 255f;
    }
    
    /**
     * Gets the blue value of the color the particle will
     * fade to. <b>(Value range is 0f-1f)</b>
     *
     * @return the red value of the second color.
     */
    public float getFadeBlue() {
        return fadeBlue / 255f;
    }
    
    /**
     * Creates a new instance of the nms counterpart
     * of this class.
     * <p>
     * Please note that this class is not supported in
     * any versions before 1.17 and could lead to errors
     * if used in legacy versions.
     *
     * @return the nms counterpart of this class.
     */
    @Override
    public Object toNMSData() {
        if (ReflectionUtils.MINECRAFT_VERSION < 17 || getEffect() != ParticleEffect.DUST_COLOR_TRANSITION)
            return null;
        Object fadeStart = ReflectionUtils.createVector3fa(getRed(), getGreen(), getBlue());
        Object fadeEnd = ReflectionUtils.createVector3fa(getFadeRed(), getFadeGreen(), getFadeBlue());
        try {
            return ParticleConstants.PARTICLE_PARAM_DUST_COLOR_TRANSITION_CONSTRUCTOR.newInstance(fadeStart, fadeEnd, getSize());
        } catch (Exception ex) {
            return null;
        }
    }
}
