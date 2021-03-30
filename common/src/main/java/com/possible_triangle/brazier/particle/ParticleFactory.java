package com.possible_triangle.brazier.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;

public interface ParticleFactory {

    @Environment(EnvType.CLIENT)
    TextureSheetParticle create(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i);

}
