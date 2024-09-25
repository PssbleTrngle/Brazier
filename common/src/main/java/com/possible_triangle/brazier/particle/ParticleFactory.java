package com.possible_triangle.brazier.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;

public interface ParticleFactory {

    TextureSheetParticle create(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i);

}
