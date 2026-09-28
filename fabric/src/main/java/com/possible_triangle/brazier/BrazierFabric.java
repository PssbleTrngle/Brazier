package com.possible_triangle.brazier;

import com.possible_triangle.brazier.config.SyncConfigMessage;
import com.possible_triangle.brazier.index.BrazierContent;
import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.item.BrazierIndicator;
import com.possible_triangle.multikulti.registrate.MultikultiRegistrate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

public class BrazierFabric implements ModInitializer, ClientModInitializer {

    private static final ResourceLocation SYNC_PACKET_ID = BrazierConstants.createId("sync_config");
    public static final MultikultiRegistrate<?> REGISTRATE = new MultikultiRegistrate<>(BrazierConstants.MOD_ID);

    @Override
    public void onInitialize() {
        Services.CONFIGS.register();
        BrazierContent.init();
        REGISTRATE.register();

        PayloadTypeRegistry.playS2C().register(SyncConfigMessage.TYPE.type(), SyncConfigMessage.TYPE.codec());

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerList().getPlayers().forEach(BrazierIndicator::playerTick);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var packet = SyncConfigMessage.create();
            ServerPlayNetworking.send(handler.player, packet);
        });

        setupLootInjects();
    }

    private void setupLootInjects() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            var id = key.location();

            if (id.equals(BuiltInLootTables.JUNGLE_TEMPLE) && Services.CONFIGS.server().injectJungleLoot()) {
                injectLoot(builder, "flame_jungle_temple");
            }

            if (Services.PLATFORM.isModLoaded("nether_extension")) return;

            if (id.equals(Blocks.NETHER_WART.getLootTable())) {
                injectLoot(builder, "warped_wart");
            }

            if (Services.PLATFORM.isModLoaded("supplementaries")) return;

            if (id.equals(EntityType.WITHER_SKELETON.getDefaultLootTable())) {
                injectLoot(builder, "wither_ash");
            }
        });
    }

    private void injectLoot(LootTable.Builder into, String name) {
        var from = ResourceKey.create(Registries.LOOT_TABLE, BrazierConstants.createId(name).withPrefix("inject/"));
        into.withPool(LootPool.lootPool()
                .add(NestedLootTable.lootTableReference(from))
        );
    }

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncConfigMessage.TYPE.type(), (packet, context) -> packet.handle());
    }
}
