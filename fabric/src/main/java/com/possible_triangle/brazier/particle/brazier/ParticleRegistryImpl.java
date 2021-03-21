package com.possible_triangle.brazier.particle.brazier;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.particle.ParticleFactory;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ParticleRegistryImpl {

    public static Supplier<SimpleParticleType> register(String name, ParticleFactory factory) {
        ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_PARTICLES).register((atlasTexture, registry) -> registry.register(new ResourceLocation(Brazier.MOD_ID, name)));

        SimpleParticleType type = FabricParticleTypes.simple();
        return () -> type;
    }

}
