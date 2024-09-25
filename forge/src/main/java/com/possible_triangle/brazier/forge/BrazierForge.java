package com.possible_triangle.brazier.forge;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.item.BrazierIndicator;
import com.possible_triangle.brazier.item.LazySpawnEgg;
import com.possible_triangle.brazier.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Brazier.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrazierForge {

    public BrazierForge() {
        Brazier.init();

        Services.CONFIGS.register();

        MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent event) -> BrazierIndicator.playerTick(event.player));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        Content.CRAZED_SPAWN_EGG.ifPresent(egg ->
                event.getItemColors().register(LazySpawnEgg::getColor, egg)
        );
    }

    @SubscribeEvent
    public static void setup(final RegisterEvent event) {
        if (event.getRegistryKey() == Registries.ITEM) {
            Brazier.setup();
        }
    }

    @SubscribeEvent
    public static void doClientStuff(final FMLClientSetupEvent event) {
        Brazier.clientSetup();
    }

}