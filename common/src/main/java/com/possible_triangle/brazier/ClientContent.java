package com.possible_triangle.brazier;

import com.possible_triangle.brazier.particle.FlameParticle;
import com.possible_triangle.brazier.particle.ParticleRegistry;

public class ClientContent {

    private ClientContent() {}

    public static void registerParticles() {
        Content.FLAME_PARTICLE.ifPresent(type -> ParticleRegistry.registerFactory(type, FlameParticle::new));
    }

}
