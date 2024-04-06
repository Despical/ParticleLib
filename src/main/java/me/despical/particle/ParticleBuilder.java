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

import me.despical.particle.utils.NMSUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import me.despical.particle.data.ParticleData;
import me.despical.particle.data.color.RegularColor;

import java.awt.*;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * A builder for particle packets.
 *
 * @author ByteZ
 * @since 24/01/2020
 */
public class ParticleBuilder {

    /**
     * The {@link ParticleEffect} that should be displayed by the client.
     */
    private ParticleEffect particle;
    /**
     * The {@link Location} of the particle.
     */
    private Location location;
    /**
     * This field has three uses:
     * <p>
     * The offsetX defines in which x oriented range the particles can
     * spawn.
     * <p>
     * It represents the x velocity a particle with the
     * {@link PropertyType#DIRECTIONAL} property should have.
     * <p>
     * It sets the red value of a {@link PropertyType#COLORABLE}
     * particle. However, since 1.13 a ParticleParam has to be used to set
     * the colors of redstone.
     */
    private float offsetX = 0;
    /**
     * This field has three uses:
     * <p>
     * The offsetY defines in which y oriented range the particles can
     * spawn.
     * <p>
     * It represents the y velocity a particle with the
     * {@link PropertyType#DIRECTIONAL}  property should have.
     * <p>
     * It sets the green value of a {@link PropertyType#COLORABLE}
     * particle. However, since 1.13 a ParticleParam has to be used to set
     * the colors of redstone.
     */
    private float offsetY = 0;
    /**
     * This field has three uses:
     * <p>
     * The offsetZ defines in which z oriented range the particles can
     * spawn.
     * <p>
     * It represents the z velocity a particle with the
     * {@link PropertyType#DIRECTIONAL} property should have.
     * <p>
     * It sets the blue value of a {@link PropertyType#COLORABLE}
     * particle. However, since 1.13 a ParticleParam has to be used to set
     * the colors of redstone.
     */
    private float offsetZ = 0;
    /**
     * Normally this field is used to multiply the velocity of a
     * particle by the given speed. There are however some special cases
     * where this value is used for something different. (e.g. {@link ParticleEffect#NOTE}).
     */
    private float speed = 1;
    /**
     * The amount of particles that should be spawned. For the extra data defined
     * in offsetX, offsetY and offsetZ to work the amount has to be set to {@code 0}.
     */
    private int amount = 0;
    /**
     * The data of the particle which should be displayed. This data contains additional
     * information the client needs to display the particle correctly.
     */
    private ParticleData particleData = null;

    /**
     * Initializes a new {@link ParticleBuilder}
     *
     * @param particle The {@link ParticleEffect} of the builder.
     * @param location The location at which the particle should be displayed
     */
    public ParticleBuilder(ParticleEffect particle, Location location) {
        this.particle = particle;
        this.location = location;
    }

    /**
     * Initializes a new {@link ParticleBuilder}
     *
     * @param particle The {@link ParticleEffect} of the builder.
     */
    public ParticleBuilder(ParticleEffect particle) {
        this.particle = particle;
        this.location = null;
    }

    public ParticleBuilder setParticle(ParticleEffect particle) {
        this.particle = particle;
        return this;
    }

    /**
     * The {@link ParticleEffect} that should be displayed by the client.
     */
    public ParticleEffect getParticle() {
        return particle;
    }

    /**
     * Sets the {@link Location} of the particle.
     *
     * @param location The new {@link Location} of the particle.
     * @return the current instance to support building operations
     */
    public ParticleBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    /**
     * The {@link Location} of the particle.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the X offset.
     *
     * @param offsetX The new value of the {@link #offsetX} field
     * @return the current instance to support building operations
     */
    public ParticleBuilder setOffsetX(float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    /**
     * This field has three uses:
     * <p>
     * The offsetX defines in which x oriented range the particles can
     * spawn.
     * <p>
     * It represents the x velocity a particle with the
     * {@link PropertyType#DIRECTIONAL} property should have.
     * <p>
     * It sets the red value of a {@link PropertyType#COLORABLE}
     * particle. However, since 1.13 a ParticleParam has to be used to set
     * the colors of redstone.
     */
    public float getOffsetX() {
        return offsetX;
    }

    /**
     * Sets the Y offset.
     *
     * @param offsetY The new value of the {@link #offsetY} field
     * @return the current instance to support building operations
     */
    public ParticleBuilder setOffsetY(float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    /**
     * Sets the offset.
     *
     * @param offsetX The new value of the {@link #offsetX} field
     * @param offsetY The new value of the {@link #offsetY} field
     * @param offsetZ The new value of the {@link #offsetZ} field
     * @return the current instance to support building operations
     */
    public ParticleBuilder setOffset(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        return this;
    }

    /**
     * This field has three uses:
     * <p>
     * The offsetY defines in which y oriented range the particles can
     * spawn.
     * <p>
     * It represents the y velocity a particle with the
     * {@link PropertyType#DIRECTIONAL}  property should have.
     * <p>
     * It sets the green value of a {@link PropertyType#COLORABLE}
     * particle. However, since 1.13 a ParticleParam has to be used to set
     * the colors of redstone.
     */
    public float getOffsetY() {
        return offsetY;
    }

    /**
     * Sets the offset.
     *
     * @param offset a {@link Vector} containing the offset values.
     * @return the current instance to support building operations
     */
    public ParticleBuilder setOffset(Vector offset) {
        this.offsetX = (float) offset.getX();
        this.offsetY = (float) offset.getY();
        this.offsetZ = (float) offset.getZ();
        return this;
    }

    /**
     * Sets the Z offset.
     *
     * @param offsetZ The new value of the {@link #offsetZ} field
     * @return the current instance to support building operations
     */
    public ParticleBuilder setOffsetZ(float offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    /**
     * This field has three uses:
     * <p>
     * The offsetZ defines in which z oriented range the particles can
     * spawn.
     * <p>
     * It represents the z velocity a particle with the
     * {@link PropertyType#DIRECTIONAL} property should have.
     * <p>
     * It sets the blue value of a {@link PropertyType#COLORABLE}
     * particle. However, since 1.13 a ParticleParam has to be used to set
     * the colors of redstone.
     */
    public float getOffsetZ() {
        return offsetZ;
    }

    /**
     * Sets the speed.
     *
     * @param speed The new value of the {@link #speed} field
     * @return the current instance to support building operations
     */
    public ParticleBuilder setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    /**
     * Normally this field is used to multiply the velocity of a
     * particle by the given speed. There are however some special cases
     * where this value is used for something different. (e.g. {@link ParticleEffect#NOTE}).
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the amount.
     *
     * @param amount The new value of the {@link #amount} field
     * @return the current instance to support building operations
     */
    public ParticleBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * The amount of particles that should be spawned. For the extra data defined
     * in offsetX, offsetY and offsetZ to work the amount has to be set to {@code 0}.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the particleData.
     *
     * @param particleData The new value of the {@link #particleData} field
     * @return the current instance to support building operations
     */
    public ParticleBuilder setParticleData(ParticleData particleData) {
        this.particleData = particleData;
        return this;
    }

    /**
     * The data of the particle which should be displayed. This data contains additional
     * information the client needs to display the particle correctly.
     */
    public ParticleData getParticleData() {
        return particleData;
    }

    /**
     * Sets the color of the particle. Note that particle
     * needs the {@link PropertyType#COLORABLE} PropertyType
     * to work.
     *
     * @param color the {@link Color} of the particle.
     * @return the current instance to support building operations
     */
    public ParticleBuilder setColor(Color color) {
        if (this.particle.hasProperty(PropertyType.COLORABLE))
            this.particleData = new RegularColor(color);
        return this;
    }

    /**
     * Creates a new {@link ParticlePacket} wit the given values.
     *
     * @return the new {@link ParticlePacket}
     * @throws IllegalStateException if the location field isn't set yet.
     */
    public Object toPacket() {
        if (location == null)
            throw new IllegalStateException("Missing location of particle.");
        if (this.particleData != null)
            this.particleData.setEffect(this.particle);
        ParticlePacket packet = new ParticlePacket(this.particle, this.offsetX, this.offsetY, this.offsetZ, this.speed, this.amount, this.particleData);
        return packet.createPacket(this.location);
    }

    /**
     * Displays the given particle to all players.
     */
    public void display() {
		NMSUtils.display(toPacket(), particle, location, amount, location.getWorld().getPlayers());
    }

    /**
     * Displays the given particle to the players in the array.
     *
     * @param players The players that should see the particle.
     */
    public void display(Player... players) {
        NMSUtils.display(toPacket(), particle, location, amount, players);
    }

    /**
     * Display the given particle to online player that match the given filter.
     *
     * @param filter a {@link Predicate} to filter out
     *               specific {@link Player Players}.
     */
    public void display(Predicate<Player> filter) {
        NMSUtils.display(toPacket(), particle, location, amount, filter);
    }

    /**
     * Displays the given particle to all players in the {@link Collection}
     *
     * @param players a list of players that should receive the particle packet.
     */
    public void display(Collection<? extends Player> players) {
		NMSUtils.display(toPacket(), particle, location, amount, players);
    }

}
