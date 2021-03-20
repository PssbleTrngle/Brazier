package com.possible_triangle.brazier;

import com.possible_triangle.brazier.block.BrazierBlock;
import com.possible_triangle.brazier.config.ClientConfig;
import com.possible_triangle.brazier.config.LoginHandler;
import com.possible_triangle.brazier.config.ServerConfig;
import com.possible_triangle.brazier.item.BrazierIndicator;
import com.possible_triangle.brazier.network.BrazierNetwork;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.architectury.event.events.TickEvent;
import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public class Brazier {

    public static final String MODID = "brazier";

    public static Supplier<ServerConfig> SERVER_CONFIG;
    public static Supplier<ClientConfig> CLIENT_CONFIG;

    public static void init() {

        SERVER_CONFIG = AutoConfig.register(ServerConfig.class, Toml4jConfigSerializer::new)::getConfig;
        CLIENT_CONFIG = AutoConfig.register(ClientConfig.class, Toml4jConfigSerializer::new)::getConfig;

        Content.init();
        BrazierNetwork.init();

        TickEvent.PLAYER_POST.register(BrazierIndicator::playerTick);
        EntityEvent.ADD.register(BrazierBlock::mobSpawn);
        PlayerEvent.PLAYER_JOIN.register(LoginHandler::playerJoined);
    }

    public static void setup() {
        Content.setup();
    }

    public static void clientSetup() {
        Content.clientSetup();
    }

}
