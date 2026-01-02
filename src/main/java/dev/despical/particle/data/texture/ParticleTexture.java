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

package dev.despical.particle.data.texture;

import dev.despical.particle.ParticleEffect;
import dev.despical.particle.PropertyType;
import dev.despical.particle.data.ParticleData;
import org.bukkit.Material;

/**
 * An implementation of {@link ParticleData} to support particles that require a texture
 * to function properly.
 *
 * @author ByteZ
 * @see PropertyType#REQUIRES_BLOCK
 * @see PropertyType#REQUIRES_ITEM
 * @since 11.06.2019
 */
public class ParticleTexture extends ParticleData {

	/**
	 * The {@link Material} that should be displayed by the particle.
	 */
	private final Material material;
	/**
	 * The damage data to be displayed by the given texture.
	 */
	private final byte data;

	/**
	 * Initializes a new {@link ParticleData} object.
	 *
	 * @param material the {@link Material} the particle should display.
	 * @param data     the damage value that should influence the texture.
	 */
	ParticleTexture(Material material, byte data) {
		this.material = material;
		this.data = data;
	}

	/**
	 * Gets the {@link Material} that will be displayed b the particle.
	 *
	 * @return the {@link Material} the current data is assigned to
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Gets the damage value that will be displayed by the client.
	 *
	 * @return the damage value of the current texture.
	 */
	public byte getData() {
		return data;
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
		//noinspection deprecation
		int id = getMaterial().getId();
		byte data = getData();
		return getEffect() == ParticleEffect.ITEM_CRACK
				? new int[]{id, data}
				: new int[]{id | data << 12};
	}
}
