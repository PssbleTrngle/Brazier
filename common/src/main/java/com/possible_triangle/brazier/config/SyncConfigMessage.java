package com.possible_triangle.brazier.config;

import com.possible_triangle.brazier.platform.Services;
import net.minecraft.network.FriendlyByteBuf;

public class SyncConfigMessage {

    private final IServerConfig config;

    private SyncConfigMessage(IServerConfig config) {
        this.config = config;
    }

    public static SyncConfigMessage create() {
        return new SyncConfigMessage(Services.CONFIGS.server());
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

    public void handle() {
        Services.CONFIGS.receiveSyncedConfig(config);
    }

}
