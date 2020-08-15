package com.possible_triangle.brazier;

import com.possible_triangle.brazier.config.BrazierConfig;
import com.possible_triangle.brazier.network.BrazierNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Brazier.MODID)
public class Brazier {

    public static final String MODID = "brazier";

    public Brazier() {
        Content.init();
        BrazierNetwork.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ModConfig.class);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BrazierConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BrazierConfig.SERVER_SPEC);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Content.setup();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Content.clientSetup(event.getMinecraftSupplier().get());
    }

}
