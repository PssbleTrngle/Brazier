package com.possible_triangle.brazier.particle.fabric;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.particle.ParticleFactory;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ParticleRegistryImpl {

    private static final HashMap<SimpleParticleType, ParticleFactory> PARTICLES = new HashMap<>();

    public static void register() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        PARTICLES.forEach((type, factory) ->
                registry.register(type, sprites -> (options, world, d, e, f, g, h, i) -> {
                    TextureSheetParticle particle = factory.create(world, d, e, f, g, h, i);
                    particle.pickSprite(sprites);
                    return particle;
                })
        );
    }

    public static Supplier<SimpleParticleType> register(String name, ParticleFactory factory) {
        SimpleParticleType type = Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(Brazier.MOD_ID, name), FabricParticleTypes.simple());

        PARTICLES.put(type, factory);
        return () -> type;
    }

}
