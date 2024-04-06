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

package me.despical.particle;

import me.despical.particle.data.SculkChargeData;
import me.despical.particle.data.ShriekData;
import me.despical.particle.data.color.DustData;
import me.despical.particle.data.color.NoteColor;
import me.despical.particle.data.color.ParticleColor;
import me.despical.particle.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.despical.particle.data.ParticleData;
import me.despical.particle.data.VibrationData;
import me.despical.particle.data.color.RegularColor;
import me.despical.particle.data.texture.BlockTexture;
import me.despical.particle.data.texture.ItemTexture;

import java.lang.reflect.Constructor;

import static me.despical.particle.ParticleConstants.PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR;
import static me.despical.particle.ParticleEffect.*;


/**
 * Represents the nms "PacketPlayOutWorldParticles" packet.
 *
 * @author ByteZ
 * @since 10.06.2019
 */
public final class ParticlePacket {
    
    /**
     * The {@link me.despical.particle.ParticleEffect} which should be displayed by the client.
     */
    private final me.despical.particle.ParticleEffect particle;
    /**
     * This field has three uses:
     * <p>
     * The offsetX defines in which x oriented range the particles can
     * spawn.
     * <p>
     * It represents the x velocity a particle with the
     * {@link me.despical.particle.PropertyType#DIRECTIONAL} property should have.
     * <p>
     * It sets the red value of a {@link me.despical.particle.PropertyType#COLORABLE}
     * particle. However, since 1.13 a ParticleParam has to be used to set
     * the colors of redstone.
     */
    private final float offsetX;
    /**
     * This field has three uses:
     * <p>
     * The offsetY defines in which y oriented range the particles can
     * spawn.
     * <p>
     * It represents the y velocity a particle with the
     * {@link me.despical.particle.PropertyType#DIRECTIONAL}  property should have.
     * <p>
     * It sets the green value of a {@link me.despical.particle.PropertyType#COLORABLE}
     * particle. However, since 1.13 a  ParticleParam has to be used to set
     * the colors of redstone.
     */
    private final float offsetY;
    /**
     * This field has three uses:
     * <p>
     * The offsetZ defines in which z oriented range the particles can
     * spawn.
     * <p>
     * It represents the z velocity a  particle with the
     * {@link me.despical.particle.PropertyType#DIRECTIONAL}  property should have.
     * <p>
     * It sets the blue value of a {@link me.despical.particle.PropertyType#COLORABLE}
     * particle. However, since 1.13 a ParticleParam has to be used to set
     * the colors of redstone.
     */
    private final float offsetZ;
    /**
     * Normally this field is used to multiply the velocity of a
     * particle by the given speed. There  are however some special cases
     * where this value is used for something different. (e.g. {@link me.despical.particle.ParticleEffect#NOTE}).
     */
    private final float speed;
    /**
     * The amount of particles that should be spawned. For the extra data defined
     * in offsetX, offsetY and offsetZ to work the amount has to be set to {@code 0}.
     */
    private final int amount;
    /**
     * The data of the particle which should be displayed. This data contains additional
     * information the client needs to display  the particle correctly.
     */
    private final ParticleData particleData;
    
    /**
     * Creates a new {@link ParticlePacket} that can be sent to one or multiple
     * {@link Player players}.
     *
     * @param particle     the {@link me.despical.particle.ParticleEffect} that should be sent.
     * @param offsetX      the offsetX or extra data the particle should have.
     * @param offsetY      the offsetY or extra data the particle should have.
     * @param offsetZ      the offsetZ or extra data the particle should have.
     * @param speed        the multiplier of the velocity.
     * @param amount       the amount of particles that should be spawned.
     * @param particleData the {@link ParticleData} of the particle
     * @see #particle
     * @see #offsetX
     * @see #offsetY
     * @see #offsetZ
     * @see #speed
     * @see #amount
     * @see #particleData
     * @see ParticleData
     */
    public ParticlePacket(me.despical.particle.ParticleEffect particle, float offsetX, float offsetY, float offsetZ, float speed, int amount, ParticleData particleData) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.amount = amount;
        if (ReflectionUtils.MINECRAFT_VERSION > 17) {
            if (particle == BARRIER) {
                this.particle = BLOCK_MARKER;
                this.particleData = new BlockTexture(Material.BARRIER);
                this.particleData.setEffect(BLOCK_MARKER);
                return;
            } else if (particle == LIGHT) {
                this.particle = BLOCK_MARKER;
                this.particleData = new BlockTexture(Material.LIGHT);
                this.particleData.setEffect(BLOCK_MARKER);
                return;
            }
        }
        this.particle = particle;
        this.particleData = particleData;
    }
    
    /**
     * Creates a new {@link ParticlePacket} that can be sent to one or multiple
     * {@link Player players}.
     *
     * @param particle the {@link me.despical.particle.ParticleEffect} that should be sent.
     * @param offsetX  the offsetX or extra data the particle should have.
     * @param offsetY  the offsetY or extra data the particle should have.
     * @param offsetZ  the offsetZ or extra data the particle should have.
     * @param speed    the multiplier of the velocity.
     * @param amount   the amount of particles that should be spawned.
     * @see #particle
     * @see #offsetX
     * @see #offsetY
     * @see #offsetZ
     * @see #speed
     * @see #amount
     */
    public ParticlePacket(me.despical.particle.ParticleEffect particle, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        this(particle, offsetX, offsetY, offsetZ, speed, amount, null);
    }
    
    /**
     * Gets the {@link me.despical.particle.ParticleEffect} that will be displayed by the client.
     *
     * @return The {@link me.despical.particle.ParticleEffect} which should be displayed by the client.
     */
    public me.despical.particle.ParticleEffect getParticle() {
        return particle;
    }
    
    /**
     * Gets the offsetX value of the particle.
     *
     * @return the offsetX value.
     */
    public float getOffsetX() {
        return offsetX;
    }
    
    /**
     * Gets the offsetY value of the particle.
     *
     * @return the offsetY value.
     */
    public float getOffsetY() {
        return offsetY;
    }
    
    /**
     * Gets the offsetZ value of the particle.
     *
     * @return the offsetZ value.
     */
    public float getOffsetZ() {
        return offsetZ;
    }
    
    /**
     * Gets the speed at which the particle will fly off.
     *
     * @return the speed of the particle.
     */
    public float getSpeed() {
        return speed;
    }
    
    /**
     * Gets how many particles will be shown by the client.
     *
     * @return the amount of particles to be spawned.
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * Gets the {@link ParticleData} that should be used when displaying the
     * particle.
     *
     * @return the {@link ParticleData} that will be used.
     */
    public ParticleData getParticleData() {
        return particleData;
    }
    
    /**
     * Creates a NMS PacketPlayOutWorldParticles packet with the data in the current
     * {@link ParticlePacket} data.
     *
     * @param location the {@link Location} the particle should be displayed at.
     * @return a PacketPlayOutWorldParticles or {@code null} when something goes wrong.
     */
    public Object createPacket(Location location) {
        try {
            me.despical.particle.ParticleEffect effect = getParticle();
            ParticleData data = getParticleData();
            double version = ReflectionUtils.MINECRAFT_VERSION;
            if (effect == null || effect.getFieldName().equals("NONE"))
                return null;
            if (data != null) {
                if (data.getEffect() != effect)
                    return null;
                Object nmsData = data.toNMSData();
                if (nmsData == null)
                    return null;
                if ((data instanceof DustData && version >= 13)
                    || (data instanceof VibrationData && version >= 17)
                    || (data instanceof ShriekData && version >= 19)
                    || (data instanceof SculkChargeData && version >= 19)
                    || (data instanceof RegularColor && (version >= 17 && effect.hasProperty(me.despical.particle.PropertyType.DUST))))
                    return createGenericParticlePacket(location, nmsData);
                if ((data instanceof BlockTexture && effect.hasProperty(me.despical.particle.PropertyType.REQUIRES_BLOCK))
                    || (data instanceof ItemTexture && effect.hasProperty(me.despical.particle.PropertyType.REQUIRES_ITEM)))
                    return createTexturedParticlePacket(location, nmsData);
                if (data instanceof ParticleColor && effect.hasProperty(me.despical.particle.PropertyType.COLORABLE))
                    return createColoredParticlePacket(location, nmsData);
                return null;
            } else if (!effect.hasProperty(me.despical.particle.PropertyType.REQUIRES_BLOCK) && !effect.hasProperty(me.despical.particle.PropertyType.REQUIRES_ITEM))
                return createPacket(effect.getNMSObject(),
                    (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                    getOffsetX(), getOffsetY(), getOffsetZ(),
                    getSpeed(), getAmount(), new int[0]);
        } catch (Exception ignored) {
        }
        return null;
    }
    
    /**
     * Creates a new packet for particles that don't need any extra checks.
     * <p>
     * <b>Note: This method does not check if the given particle and
     * data match!</b>
     *
     * @param location the {@link Location} the particle should be displayed at.
     * @param param    the pre-built ParticleParam.
     * @return a PacketPlayOutWorldParticles or {@code null} when something goes wrong.
     */
    private Object createGenericParticlePacket(Location location, Object param) {
        return createPacket(param,
            (float) location.getX(), (float) location.getY(), (float) location.getZ(),
            getOffsetX(), getOffsetY(), getOffsetZ(),
            getSpeed(), getAmount(), new int[0]
        );
    }
    
    /**
     * Creates a new packet for particles that support custom textures.
     * <p>
     * <b>Note: This method does not check if the given particle and
     * data match!</b>
     *
     * @param location the {@link Location} the particle should be displayed at.
     * @param param    the pre-built ParticleParam.
     * @return a PacketPlayOutWorldParticles or {@code null} when something goes wrong.
     * @see me.despical.particle.PropertyType#REQUIRES_BLOCK
     * @see me.despical.particle.PropertyType#REQUIRES_ITEM
     */
    private Object createTexturedParticlePacket(Location location, Object param) {
        me.despical.particle.ParticleEffect effect = getParticle();
        double version = ReflectionUtils.MINECRAFT_VERSION;
        return createPacket(version < 13 ? effect.getNMSObject() : param,
            (float) location.getX(), (float) location.getY(), (float) location.getZ(),
            getOffsetX(), getOffsetY(), getOffsetZ(),
            getSpeed(), getAmount(), version < 13 ? (int[]) param : new int[0]
        );
    }
    
    /**
     * Creates a new packet for particles that support custom colors.
     * <p>
     * <b>Note: This method does not check if the given particle and
     * data match!</b>
     *
     * @param location the {@link Location} the particle should be displayed at.
     * @param param    the pre-built ParticleParam.
     * @return a PacketPlayOutWorldParticles or {@code null} when something goes wrong.
     * @see PropertyType#COLORABLE
     */
    private Object createColoredParticlePacket(Location location, Object param) {
        ParticleEffect effect = getParticle();
        ParticleData data = getParticleData();
        if (data instanceof NoteColor && effect.equals(NOTE)) {
            return createPacket(effect.getNMSObject(),
                (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                ((NoteColor) data).getRed(), 0f, 0f,
                getSpeed(), getAmount(), new int[0]
            );
        } else if (data instanceof RegularColor) {
            RegularColor color = ((RegularColor) data);
            if (ReflectionUtils.MINECRAFT_VERSION < 13 || !effect.equals(REDSTONE)) {
                return createPacket(effect.getNMSObject(),
                    (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                    (effect.equals(REDSTONE) && color.getRed() == 0 ? Float.MIN_NORMAL : color.getRed()), color.getGreen(), color.getBlue(),
                    1f, 0, new int[0]
                );
            } else {
                return createPacket(param,
                    (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                    getOffsetX(), getOffsetY(), getOffsetZ(),
                    getSpeed(), getAmount(), new int[0]
                );
            }
        } else return null;
    }
    
    /**
     * Creates a new PacketPlayOutWorldParticles
     * object with the given data.
     *
     * @param param     the ParticleParam of the  packet.
     * @param locationX the x coordinate of the location the particle
     *                  should be displayed at.
     * @param locationY the y coordinate of the location the particle
     *                  should be displayed at.
     * @param locationZ the z coordinate of the location the particle
     *                  should be displayed at.
     * @param offsetX   the offset x value of the packet.
     * @param offsetY   the offset y value of the packet.
     * @param offsetZ   the offset z value of the packet.
     * @param speed     the speed of the particle.
     * @param amount    the amount of particles.
     * @param data      extra data for the particle.
     * @return A PacketPlayOutWorldParticles instance with the given data or {@code null} if an error occurs.
     */
    private Object createPacket(Object param, float locationX, float locationY, float locationZ, float offsetX, float offsetY, float offsetZ, float speed, int amount, int[] data) {
        Constructor packetConstructor = PACKET_PLAY_OUT_WORLD_PARTICLES_CONSTRUCTOR;
        try {
            if (ReflectionUtils.MINECRAFT_VERSION < 13)
                return packetConstructor.newInstance(param, true, locationX, locationY, locationZ, offsetX, offsetY, offsetZ, speed, amount, data);
            if (ReflectionUtils.MINECRAFT_VERSION < 15)
                return packetConstructor.newInstance(param, true, locationX, locationY, locationZ, offsetX, offsetY, offsetZ, speed, amount);
            return packetConstructor.newInstance(param, true, (double) locationX, (double) locationY, (double) locationZ, offsetX, offsetY, offsetZ, speed, amount);
        } catch (Exception ex) {
            return null;
        }
    }
    
}
