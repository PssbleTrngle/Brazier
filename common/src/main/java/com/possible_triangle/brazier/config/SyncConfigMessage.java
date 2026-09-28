package com.possible_triangle.brazier.config;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class SyncConfigMessage implements CustomPacketPayload {

    public static final TypeAndCodec<FriendlyByteBuf, SyncConfigMessage> TYPE = new TypeAndCodec<>(
            new Type<>(BrazierConstants.createId("sync_config")),
            StreamCodec.of(SyncConfigMessage::encode, SyncConfigMessage::decode)
    );

    private final IServerConfig config;

    private SyncConfigMessage(IServerConfig config) {
        this.config = config;
    }

    public static SyncConfigMessage create() {
        return new SyncConfigMessage(Services.CONFIGS.server());
    }

    private static void encode(FriendlyByteBuf buf, SyncConfigMessage message) {
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

    private static SyncConfigMessage decode(FriendlyByteBuf buf) {
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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }
}
