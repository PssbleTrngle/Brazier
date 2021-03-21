package com.possible_triangle.brazier.forge;

import com.possible_triangle.brazier.Brazier;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Brazier.MOD_ID)
public class BrazierForge {

    public BrazierForge () {
        EventBuses.registerModEventBus(Brazier.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        Brazier.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        Brazier.setup();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Brazier.clientSetup();
    }

}