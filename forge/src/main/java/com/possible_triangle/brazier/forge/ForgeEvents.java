package com.possible_triangle.brazier.forge;

import com.possible_triangle.brazier.block.BrazierBlock;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    @SubscribeEvent
    public static void onMobSpawn(LivingSpawnEvent.CheckSpawn event) {
        if(BrazierBlock.prevents(event.getEntity(), event.getWorld(), event.getSpawnReason())) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onLootLoaded(LootTableLoadEvent event) {
        //Conditional.injectLoot(event.getName(), p -> event.getTable().addPool(p.build()));
    }

}
