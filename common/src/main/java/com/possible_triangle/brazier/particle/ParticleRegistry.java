package com.possible_triangle.brazier.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleType;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class ParticleRegistry {

    private static final HashMap<ParticleType<?>, ParticleFactory> PARTICLES = new HashMap<>();

    public static void register(BiConsumer<ParticleType<?>,ParticleFactory> consumer) {
        PARTICLES.forEach(consumer);
    }

    @Environment(EnvType.CLIENT)
    public static void registerFactory(ParticleType<?> type, ParticleFactory factory) {
        PARTICLES.put(type, factory);
    }

}
