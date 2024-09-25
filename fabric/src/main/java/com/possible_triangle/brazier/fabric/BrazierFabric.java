package com.possible_triangle.brazier.fabric;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.item.BrazierIndicator;
import com.possible_triangle.brazier.item.LazySpawnEgg;
import com.possible_triangle.brazier.network.BrazierNetwork;
import com.possible_triangle.brazier.network.SyncConfigMessage;
import com.possible_triangle.brazier.particle.fabric.ParticleRegistryImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class BrazierFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        Brazier.init();
        Brazier.setup();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerList().getPlayers().forEach(BrazierIndicator::playerTick);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            BrazierNetwork.CHANNEL.sendToPlayer(handler.player, new SyncConfigMessage(Brazier.serverConfig()));
        });
    }

    @Override
    public void onInitializeClient() {
        Brazier.clientSetup();
        ParticleRegistryImpl.register();

        Content.CRAZED_SPAWN_EGG.ifPresent(egg ->
                ColorProviderRegistry.ITEM.register(LazySpawnEgg::getColor, egg)
        );
    }
}
