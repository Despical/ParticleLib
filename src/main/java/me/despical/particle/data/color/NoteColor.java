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


import me.despical.particle.utils.MathUtils;
import me.despical.particle.ParticleEffect;
import me.despical.particle.data.ParticleData;

/**
 * An implementation of the {@link ParticleColor} class to support note colors.
 *
 * @author ByteZ
 * @see ParticleEffect#NOTE
 * @since 10.06.2019
 */
public final class NoteColor extends ParticleColor {
    
    /**
     * Initializes a new {@link ParticleData} object.
     *
     * @param note the note that should be displayed.
     */
    public NoteColor(int note) {
        super(MathUtils.getMaxOrMin(note, 24, 0), 0, 0);
        setEffect(ParticleEffect.NOTE);
    }
    
    /**
     * Sets the {@link ParticleEffect}.
     *
     * @param effect the {@link ParticleEffect} that should be displayed.
     */
    @Override
    public void setEffect(ParticleEffect effect) {
        super.setEffect(ParticleEffect.NOTE);
    }
    
    /**
     * Gets the red value of the color.
     *
     * @return the red value.
     */
    @Override
    public float getRed() {
        return super.getRed() / 24f;
    }
    
    /**
     * Returns 0 because the offsetY isn't used by the color of notes.
     *
     * @return 0.
     */
    @Override
    public float getGreen() {
        return 0;
    }
    
    /**
     * Returns 0 because the offsetZ isn't used by the color of notes.
     *
     * @return 0.
     */
    @Override
    public float getBlue() {
        return 0;
    }
    
    /**
     * Converts the current {@link ParticleData} instance into nms data. If the current
     * minecraft version was released before 1.13 an int array should be returned. If the
     * version was released after 1.12 a nms "ParticleParam" has to be returned.
     *
     * @return the nms data.
     */
    @Override
    public Object toNMSData() {
        return new int[0];
    }
    
    /**
     * Generates a random {@link NoteColor} instance to support rainbow trails,
     * cloaks and other effects that can be constructed using the note particle.
     *
     * @return a random {@link NoteColor} instance.
     */
    public static NoteColor random() {
        return new NoteColor(MathUtils.generateRandomInteger(0, 24));
    }
    
}
