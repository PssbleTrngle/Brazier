package com.possible_triangle.brazier;

import com.possible_triangle.brazier.config.SyncConfigMessage;
import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.item.BrazierIndicator;
import com.possible_triangle.multikulti.registrate.MultikultiRegistrate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

public class BrazierFabric implements ModInitializer, ClientModInitializer {

    private static final ResourceLocation SYNC_PACKET_ID = Brazier.createId("sync_config");
    public static final MultikultiRegistrate<?> REGISTRATE =  new MultikultiRegistrate<>(Brazier.MOD_ID);

    @Override
    public void onInitialize() {
        Brazier.init();
        REGISTRATE.register();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerList().getPlayers().forEach(BrazierIndicator::playerTick);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var buf = PacketByteBufs.create();
            var packet = SyncConfigMessage.create();
            SyncConfigMessage.encode(packet, buf);
            ServerPlayNetworking.send(handler.player, SYNC_PACKET_ID, buf);
        });

        setupLootInjects();
    }

    private void setupLootInjects() {
        LootTableEvents.MODIFY.register((resources, loot, id, table, source) -> {
            if (id.equals(BuiltInLootTables.JUNGLE_TEMPLE) && Services.CONFIGS.server().injectJungleLoot()) {
                injectLoot(table, "flame_jungle_temple");
            }

            if(Services.PLATFORM.isModLoaded("nether_extension")) return;

            if (id.equals(Blocks.NETHER_WART.getLootTable())) {
                injectLoot(table, "warped_wart");
            }

            if(Services.PLATFORM.isModLoaded("supplementaries")) return;

            if (id.equals(EntityType.WITHER_SKELETON.getDefaultLootTable())) {
                injectLoot(table, "wither_ash");
            }
        });
    }

    private void injectLoot(LootTable.Builder into, String name) {
        var from = Brazier.createId(name).withPrefix("inject/");
        into.withPool(LootPool.lootPool()
                .add(LootTableReference.lootTableReference(from))
        );
    }

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_PACKET_ID, (client, handler, buf, response) -> {
            var packet = SyncConfigMessage.decode(buf);
            packet.handle();
        });
    }
}
