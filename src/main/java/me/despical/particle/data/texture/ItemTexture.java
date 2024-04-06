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

package me.despical.particle.data.texture;

import me.despical.particle.ParticleConstants;
import me.despical.particle.PropertyType;
import me.despical.particle.data.ParticleData;
import me.despical.particle.utils.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

/**
 * An implementation of the {@link ParticleTexture} object to support item texture particles.
 *
 * @author ByteZ
 * @see PropertyType#REQUIRES_ITEM
 * @since 11.06.2019
 */
public final class ItemTexture extends ParticleTexture {
    
    /**
     * The {@link ItemStack} that will be displayed by the particle.
     */
    private final ItemStack itemStack;
    
    /**
     * Initializes a new {@link ParticleData} object.
     *
     * @param itemStack the {@link ItemStack} which
     *                  should be displayed by the
     *                  particle.
     */
    public ItemTexture(ItemStack itemStack) {
        super(itemStack == null ? null : itemStack.getType(), (byte) 0);
        this.itemStack = itemStack;
    }
    
    /**
     * Converts the current {@link ParticleData} instance into nms data. If the current
     * minecraft version was released before 1.13 an int array should be returned. If the
     * version was released after 1.12 a nms ParticleParam has to be returned.
     *
     * @return the nms data.
     */
    @Override
    public Object toNMSData() {
        if (getMaterial() == null || getData() < 0 || getEffect() == null || !getEffect().hasProperty(PropertyType.REQUIRES_ITEM))
            return null;
        if (ReflectionUtils.MINECRAFT_VERSION < 13)
            return super.toNMSData();
        else {
            try {
                return ParticleConstants.PARTICLE_PARAM_ITEM_CONSTRUCTOR.newInstance(getEffect().getNMSObject(), toNMSItemStack(getItemStack()));
            } catch (Exception ex) {
                return null;
            }
        }
    }
    
    /**
     * Gets the {@link ItemStack} that will be displayed by the particle.
     *
     * @return the assigned {@link ItemStack}.
     */
    public ItemStack getItemStack() {
        return itemStack;
    }
    
    /**
     * Gets the NMS ItemStack instance of a CraftItemSTack.
     *
     * @param itemStack the CraftItemStack
     * @return the ItemStack instance of the specified CraftItemStack or {@code null} if either the given parameter is invalid or an error occurs.
     */
    public static Object toNMSItemStack(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        try {
            return ParticleConstants.CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.invoke(null, itemStack);
        } catch (Exception ex) {
            return null;
        }
    }
    
}
