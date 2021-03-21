package com.possible_triangle.brazier.network;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.config.ServerConfig;
import me.shedaniel.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class SyncConfigMessage {

    private final byte[] configData;
    private static final ByteConfigSerializer<ServerConfig> serializer = new ByteConfigSerializer<>();

    public SyncConfigMessage(ServerConfig config) {
        this(serializer.serialize(config));
    }

    public SyncConfigMessage(byte[] configData) {
        this.configData = configData;
    }

    public static void encode(SyncConfigMessage message, FriendlyByteBuf buf) {
        buf.writeByteArray(message.configData);
    }

    public static SyncConfigMessage decode(FriendlyByteBuf buf) {
        return new SyncConfigMessage(buf.readByteArray());
    }

    public static void handle(SyncConfigMessage message, Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        context.queue(() -> {
            if (context.getEnv() == EnvType.CLIENT) {
                serializer.deserialize(message.configData).ifPresent(Brazier::setSyncedConfig);
            }
        });
    }

}
