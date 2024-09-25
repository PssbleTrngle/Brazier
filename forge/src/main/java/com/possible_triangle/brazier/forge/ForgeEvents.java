package com.possible_triangle.brazier.forge;

import com.possible_triangle.brazier.logic.BrazierLogic;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMobSpawn(MobSpawnEvent.PositionCheck event) {
        if (BrazierLogic.prevents(event.getEntity(), event.getLevel(), event.getSpawnType())) {
            event.setResult(Event.Result.DENY);
        }
    }

}
