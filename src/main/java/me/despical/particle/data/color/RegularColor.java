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

import me.despical.particle.ParticleConstants;
import me.despical.particle.ParticleEffect;
import me.despical.particle.data.ParticleData;
import me.despical.particle.utils.MathUtils;
import me.despical.particle.utils.ReflectionUtils;
import me.despical.particle.PropertyType;

import java.awt.*;

/**
 * An implementation of the {@link ParticleColor} class that supports normal RGB values.
 * <p>
 * If you want to define a custom size for {@link ParticleEffect#REDSTONE} or the second
 * color for {@link ParticleEffect#DUST_COLOR_TRANSITION}, use {@link DustData} and
 * {@link DustColorTransitionData}. You can however use this class if you're just looking
 * to set the color.
 *
 * @author ByteZ
 * @see PropertyType#COLORABLE
 * @since 10.06.2019
 */
public class RegularColor extends ParticleColor {
    
    /**
     * Initializes a new {@link ParticleData} object.
     *
     * @param color the {@link Color} the
     *              particle should have.
     */
    public RegularColor(Color color) {
        super(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    /**
     * Initializes a new {@link ParticleData} object.
     *
     * @param red   the red value of the color.
     * @param green the green value of the color.
     * @param blue  the blue value of the color.
     */
    public RegularColor(int red, int green, int blue) {
        super(MathUtils.getMaxOrMin(red, 255, 0), MathUtils.getMaxOrMin(green, 255, 0), MathUtils.getMaxOrMin(blue, 255, 0));
    }
    
    /**
     * Gets the red value of the color.
     *
     * @return the red value.
     */
    @Override
    public float getRed() {
        return super.getRed() / 255f;
    }
    
    /**
     * Gets green red value of the color.
     *
     * @return the green value.
     */
    @Override
    public float getGreen() {
        return super.getGreen() / 255f;
    }
    
    /**
     * Gets the blue value of the color.
     *
     * @return the blue value.
     */
    @Override
    public float getBlue() {
        return super.getBlue() / 255f;
    }
    
    /**
     * Converts the current {@link ParticleData} instance into nms data. If the current
     * minecraft version was released before 1.13 an int array should be returned. If the
     * version was released after 1.12 a nms "ParticleParam" has to be returned.
     * <p>
     * This method also supports TransitioningDust particles since 1.6.
     *
     * @return the nms data.
     */
    @Override
    public Object toNMSData() {
        if (ReflectionUtils.MINECRAFT_VERSION < 13 || (getEffect() != ParticleEffect.REDSTONE && getEffect() != ParticleEffect.DUST_COLOR_TRANSITION))
            return new int[0];
        try {
            if (getEffect() == ParticleEffect.REDSTONE)
                return ReflectionUtils.MINECRAFT_VERSION < 17
                    ? ParticleConstants.PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(getRed(), getGreen(), getBlue(), 1f)
                    : ParticleConstants.PARTICLE_PARAM_REDSTONE_CONSTRUCTOR.newInstance(ReflectionUtils.createVector3fa(getRed(), getGreen(), getBlue()), 1f);
            if (ReflectionUtils.MINECRAFT_VERSION < 17)
                return null;
            Object colorVector = ReflectionUtils.createVector3fa(getRed(), getGreen(), getBlue());
            return ParticleConstants.PARTICLE_PARAM_DUST_COLOR_TRANSITION_CONSTRUCTOR.newInstance(colorVector, colorVector, 1f);
        } catch (Exception ex) {
            return null;
        }
    }
    
    /**
     * Generates a random {@link RegularColor} instance with a high saturation. If you
     * want a completely random {@link Color} use {@link #random(boolean)} with false
     * as the highSaturarion parameter.
     *
     * @return a randomly generated {@link RegularColor} instance.
     */
    public static RegularColor random() {
        return random(true);
    }
    
    /**
     * Generates a random {@link RegularColor} instance. If the highSaturation parameter
     * is set to true, a random hue from the HSV spectrum will be used. Otherwise, 3 random
     * integers ranging from 0 to 255 for the RGB values will be generated.
     *
     * @param highSaturation determines if the colors should have a high saturation.
     * @return a randomly generated {@link RegularColor} instance.
     */
    public static RegularColor random(boolean highSaturation) {
        if (highSaturation)
            return fromHSVHue(MathUtils.generateRandomInteger(0, 360));
        else
            return new RegularColor(new Color(MathUtils.RANDOM.nextInt(256), MathUtils.RANDOM.nextInt(256), MathUtils.RANDOM.nextInt(256)));
    }
    
    /**
     * Constructs a {@link RegularColor} using the HSV color spectrum.
     *
     * @param hue the hue the specific color has.
     * @return a {@link RegularColor} instance with the given HSV value as its {@link Color}.
     * @see Color#HSBtoRGB(float, float, float)
     */
    public static RegularColor fromHSVHue(int hue) {
        return new RegularColor(new Color(Color.HSBtoRGB(hue / 360f, 1f, 1f)));
    }
    
}
