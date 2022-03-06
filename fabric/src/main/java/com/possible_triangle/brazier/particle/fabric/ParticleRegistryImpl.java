package com.possible_triangle.brazier.particle.fabric;

import com.possible_triangle.brazier.ClientContent;
import com.possible_triangle.brazier.particle.ParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.TextureSheetParticle;

@SuppressWarnings("unused")
public class ParticleRegistryImpl {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientContent.registerParticles();
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        ParticleRegistry.register((type, factory) ->
                registry.register(type, sprites -> (options, world, d, e, f, g, h, i) -> {
                    TextureSheetParticle particle = factory.create(world, d, e, f, g, h, i);
                    particle.pickSprite(sprites);
                    return particle;
                })
        );
    }

}
