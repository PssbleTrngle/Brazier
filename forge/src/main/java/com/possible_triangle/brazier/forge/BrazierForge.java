package com.possible_triangle.brazier.forge;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.entity.forge.EntityUtilImpl;
import com.possible_triangle.brazier.item.LazySpawnEgg;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Brazier.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrazierForge {

    public BrazierForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Brazier.MOD_ID, bus);
        Brazier.init();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        Content.CRAZED_SPAWN_EGG.ifPresent(egg ->
                event.getItemColors().register(LazySpawnEgg::getColor, egg)
        );
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        EntityUtilImpl.ATTRIBUTES.forEach(event::put);
    }

    @SubscribeEvent
    public static void setup(final RegisterEvent event) {
        if (event.getRegistryKey() == Registry.ITEM_REGISTRY) {
            Brazier.setup();
        }
    }

    @SubscribeEvent
    public static void doClientStuff(final FMLClientSetupEvent event) {
        Brazier.clientSetup();
    }

}