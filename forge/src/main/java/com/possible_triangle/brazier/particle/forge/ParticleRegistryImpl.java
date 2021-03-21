package com.possible_triangle.brazier.particle.forge;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.particle.ParticleFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ParticleRegistryImpl {

    public static final DeferredRegister<ParticleType<?>> PARTICLES_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Brazier.MOD_ID);
    private static final ArrayList<Supplier<Particle>> PARTICLES = new ArrayList<>();

    private static class Particle {
        public final SimpleParticleType type;
        public final ParticleFactory factory;

        private Particle(SimpleParticleType type, ParticleFactory factory) {
            this.type = type;
            this.factory = factory;
        }
    }

    @SubscribeEvent
    public static void register(ParticleFactoryRegisterEvent event) {
        PARTICLES.stream()
                .map(Supplier::get)
                .forEach(p -> Minecraft.getInstance().particleEngine.register(p.type, sprites ->
                        (arg, arg2, d, e, f, g, h, i) -> {
                            TextureSheetParticle particle = p.factory.create(arg2, d, e, f, g, h, i);
                            particle.pickSprite(sprites);
                            return particle;
                        }
                ));
    }

    @SuppressWarnings("unused")
    public static Supplier<SimpleParticleType> register(String name, ParticleFactory factory) {
        RegistryObject<SimpleParticleType> type = PARTICLES_TYPES.register(name, () -> new SimpleParticleType(false));
        PARTICLES.add(() -> new Particle(type.get(), factory));
        return type;
    }

}
