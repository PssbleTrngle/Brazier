package com.possible_triangle.brazier;

import com.possible_triangle.brazier.config.ClientConfig;
import com.possible_triangle.brazier.config.ServerConfig;
import com.possible_triangle.brazier.item.BrazierIndicator;
import com.possible_triangle.brazier.network.BrazierNetwork;
import com.possible_triangle.brazier.network.SyncConfigMessage;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Brazier {
    
    private Brazier() {}

    public static final String MOD_ID = "brazier";

    private static ConfigHolder<ServerConfig> localServerConfig;
    private static ServerConfig syncedServerConfig;
    private static ConfigHolder<ClientConfig> clientConfig;

    public static final Logger LOGGER = LogManager.getLogger();

    public static ClientConfig clientConfig() {
        return clientConfig.get();
    }

    public static ServerConfig serverConfig() {
        return Optional.ofNullable(syncedServerConfig).orElseGet(localServerConfig);
    }

    public static void setSyncedConfig(ServerConfig config) {
        syncedServerConfig = config;
    }

    public static void init() {

        localServerConfig = AutoConfig.register(ServerConfig.class, Toml4jConfigSerializer::new);
        clientConfig = AutoConfig.register(ClientConfig.class, Toml4jConfigSerializer::new);

        Content.init();
        BrazierNetwork.init();

        TickEvent.PLAYER_POST.register(BrazierIndicator::playerTick);
        PlayerEvent.PLAYER_JOIN.register(player ->
                BrazierNetwork.CHANNEL.sendToPlayer(player, new SyncConfigMessage(Brazier.serverConfig()))
        );
    }

    public static void setup() {
        Content.setup();
    }

    public static void clientSetup() {
        ClientContent.setup();
    }

}
