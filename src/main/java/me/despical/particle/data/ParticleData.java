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

package me.despical.particle.data;


import me.despical.particle.ParticleEffect;

/**
 * A class to easier hold data of a particle.
 *
 * @author ByteZ
 * @since 10.06.2019
 */
public abstract class ParticleData {
    
    /**
     * The {@link ParticleEffect} the current {@link ParticleData} instance is
     * assigned to.
     */
    private ParticleEffect effect;
    
    /**
     * Sets the {@link ParticleEffect}.
     *
     * @param effect the {@link ParticleEffect} that should be displayed.
     */
    public void setEffect(ParticleEffect effect) {
        this.effect = effect;
    }
    
    /**
     * Converts the current {@link ParticleData} instance into nms data. If the current
     * minecraft version was released before 1.13 an int array should be returned. If the
     * version was released after 1.12 a nms "ParticleParam" has to be returned.
     *
     * @return the nms data.
     */
    public abstract Object toNMSData();
    
    /**
     * Gets the {@link ParticleEffect} the current {@link ParticleData} is assigned to.
     *
     * @return the current {@link ParticleEffect}
     */
    public ParticleEffect getEffect() {
        return effect;
    }
}
