package com.possible_triangle.brazier.network;

import com.possible_triangle.brazier.Brazier;
import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;

public class BrazierNetwork {

    private static final String version = "1.0";

    public static final NetworkChannel CHANNEL = NetworkChannel.create(new ResourceLocation(Brazier.MOD_ID, "network"));

    public static void init() {
        CHANNEL.register(SyncConfigMessage.class, SyncConfigMessage::encode, SyncConfigMessage::decode, SyncConfigMessage::handle);
    }


}
