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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.despical.particle.utils.ReflectionUtils;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Maps classes, methods and fields to their respective names for different versions of Minecraft.
 */
public class ParticleMappings {
    
    /**
     * {@link Map} containing all mappings needed by ParticleLib.
     */
    private static final Map<String, String> mappings = new HashMap<>();
    
    static {
        double version = ReflectionUtils.MINECRAFT_VERSION;
        try (InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(ReflectionUtils.getResourceStreamSafe("mappings.json")))) {
            //noinspection deprecation - Outdated gson is used in pre 1.18 versions
            JsonArray array = version < 18
                ? new JsonParser().parse(reader).getAsJsonArray()
                : JsonParser.parseReader(reader).getAsJsonArray();
            
            for (int i = 0; i < array.size(); ++i) {
                JsonObject object = array.get(i).getAsJsonObject();
                processMapping(object, version);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not load mappings", ex);
        }
    }
    
    // private static void processMapping(JsonObject object, double version) {
    
    /**
     * Processes a mapping {@link JsonObject} and adds it to {@link #mappings} if
     * it exists in the current version of Minecraft.
     *
     * @param object  the mapping {@link JsonObject}
     * @param version the current version of Minecraft
     */
    private static void processMapping(JsonObject object, double version) {
        if (version < object.get("min").getAsDouble() || version > object.get("max").getAsDouble())
            return;
        String name = object.get("name").getAsString();
        JsonArray mappingsArray = object.get("mappings").getAsJsonArray();
        String bestMatch = null;
        double lastVersion = 0;
        for (int i = 0; i < mappingsArray.size(); ++i) {
            JsonObject mapping = mappingsArray.get(i).getAsJsonObject();
            double from = mapping.get("from").getAsDouble();
            if (version >= from && from > lastVersion)
                bestMatch = mapping.get("value").getAsString();
        }
        if (bestMatch != null)
            mappings.put(name, bestMatch);
    }
    
    /**
     * Gets the mapped {@link Class} for the given name.
     *
     * @param name the name of the class
     * @return the mapped {@link Class}
     */
    public static Class<?> getMappedClass(String name) {
        if (!mappings.containsKey(name))
            return null;
        return ReflectionUtils.getNMSClass(mappings.get(name));
    }

    /**
     * Gets the mapped {@link Method} for the given name.
     *
     * @param targetClass    the class to get the method from
     * @param name           the name of the method
     * @param parameterTypes the parameter types of the method
     * @return the mapped {@link Method}
     */
    public static Method getMappedMethod(Class<?> targetClass, String name, Class<?>... parameterTypes) {
        if (!mappings.containsKey(name))
            return null;
        return ReflectionUtils.getMethodOrNull(targetClass, mappings.get(name), parameterTypes);
    }

    /**
     * Gets the mapped {@link Field} for the given name.
     *
     * @param targetClass the class to get the field from
     * @param name        the name of the field
     * @param declared    whether to get the declared field or not
     * @return the mapped {@link Field}
     */
    public static Field getMappedField(Class<?> targetClass, String name, boolean declared) {
        if (!mappings.containsKey(name))
            return null;
        return ReflectionUtils.getFieldOrNull(targetClass, mappings.get(name), declared);
    }
    
}
