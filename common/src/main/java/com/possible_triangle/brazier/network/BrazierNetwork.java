package com.possible_triangle.brazier.network;

import com.possible_triangle.brazier.Brazier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class BrazierNetwork {

    private static final String version = "1.0";

    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(Brazier.MODID, "network"),
            () -> version,
            version::equals,
            version::equals
    );

    public static void init() {
        channel.registerMessage(1, SyncConfigMessage.class, SyncConfigMessage::encode, SyncConfigMessage::decode, SyncConfigMessage::handle);
    }

    public static void sendTo(Object message, PlayerEntity player) {
        channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

}
