package com.possible_triangle.brazier;

import com.possible_triangle.brazier.config.SyncConfigMessage;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber
public class NeoForgeNetwork {

    @SubscribeEvent
    private static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").commonToClient(
                SyncConfigMessage.TYPE.type(),
                SyncConfigMessage.TYPE.codec(),
                (message, context) -> message.handle()
        );
    }

    @SubscribeEvent
    private static void sync(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, SyncConfigMessage.create());
        }
    }

}
