package com.possible_triangle.brazier.forge;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.particle.forge.ParticleRegistryImpl;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Brazier.MOD_ID)
public class BrazierForge {

    public BrazierForge () {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Brazier.MOD_ID, bus);

        bus.addListener(this::setup);
        bus.addListener(this::doClientStuff);

        ParticleRegistryImpl.PARTICLES_TYPES.register(bus);

        Brazier.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        Brazier.setup();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Brazier.clientSetup();
    }

}