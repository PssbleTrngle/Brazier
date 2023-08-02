package com.possible_triangle.brazier.network;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.config.DistanceHandler;
import com.possible_triangle.brazier.config.IServerConfig;
import com.possible_triangle.brazier.config.SyncedServerConfig;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class SyncConfigMessage {

    private final IServerConfig config;

    public SyncConfigMessage(IServerConfig config) {
        this.config = config;
    }

    public static void encode(SyncConfigMessage message, FriendlyByteBuf buf) {
        buf.writeBoolean(message.config.injectJungleLoot());
        buf.writeBoolean(message.config.spawnCrazed());
        buf.writeDouble(message.config.crazedSpawnChance());
        buf.writeInt(message.config.maxHeight());
        buf.writeInt(message.config.rangePerLevel());
        buf.writeInt(message.config.baseRange());
        buf.writeBoolean(message.config.protectAbove());
        buf.writeEnum(message.config.distanceCalculator());
        buf.writeBoolean(message.config.enableSpawnPowder());
        buf.writeBoolean(message.config.enableDecoration());
    }

    public static SyncConfigMessage decode(FriendlyByteBuf buf) {
        return new SyncConfigMessage(
                new SyncedServerConfig(
                        buf.readBoolean(),
                        buf.readBoolean(),
                        buf.readDouble(),
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt(),
                        buf.readBoolean(),
                        buf.readEnum(DistanceHandler.Type.class),
                        buf.readBoolean(),
                        buf.readBoolean()
                )
        );
    }

    public static void handle(SyncConfigMessage message, Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        context.queue(() -> {
            if (context.getEnv() == EnvType.CLIENT) {
                Brazier.setSyncedConfig(message.config);
            }
        });
    }

}