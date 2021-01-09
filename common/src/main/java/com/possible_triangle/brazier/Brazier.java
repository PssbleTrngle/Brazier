package com.possible_triangle.brazier;

import com.possible_triangle.brazier.config.BrazierConfig;
import com.possible_triangle.brazier.item.BrazierIndicator;
import com.possible_triangle.brazier.network.BrazierNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Brazier.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Brazier {

    public static final String MODID = "brazier";

    public Brazier() {
        Content.init();
        BrazierNetwork.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BrazierConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BrazierConfig.SERVER_SPEC);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Content.setup();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Content.clientSetup(event.getMinecraftSupplier().get());
    }

    @SubscribeEvent
    static void playerTick(TickEvent.PlayerTickEvent event) {
        BrazierIndicator.playerTick(event);
    }

}
