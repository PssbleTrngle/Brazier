package com.possible_triangle.brazier.particle;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class ParticleRegistry {

    @ExpectPlatform
    public static Supplier<SimpleParticleType> register(String name, ParticleFactory factory) {
        throw new AssertionError();
    }

}
