package com.possible_triangle.brazier;

import com.possible_triangle.brazier.logic.BrazierLogic;
import com.possible_triangle.brazier.world.item.BrazierIndicator;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class NeoForgeEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    private static void onMobSpawn(MobSpawnEvent.PositionCheck event) {
        if (BrazierLogic.prevents(event.getEntity(), event.getLevel(), event.getSpawnType())) {
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }


    @SubscribeEvent
    private static void onPlayerTick(PlayerTickEvent event) {
        BrazierIndicator.playerTick(event.getEntity());
    }

}
