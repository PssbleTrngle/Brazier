package com.possible_triangle.brazier.particle.forge;

import com.possible_triangle.brazier.ClientContent;
import com.possible_triangle.brazier.particle.ParticleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistryImpl {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void register(ParticleFactoryRegisterEvent event) {
        ClientContent.registerParticles();
        ParticleRegistry.register((type, factory) -> Minecraft.getInstance().particleEngine.register(type, sprites ->
                        (arg, arg2, d, e, f, g, h, i) -> {
                            TextureSheetParticle particle = factory.create(arg2, d, e, f, g, h, i);
                            particle.pickSprite(sprites);
                            return particle;
                        }
                ));
    }

}
