package com.possible_triangle.brazier;

import com.possible_triangle.brazier.block.tile.render.BrazierRenderer;
import com.possible_triangle.brazier.config.ClientConfig;
import com.possible_triangle.brazier.config.ServerConfig;
import com.possible_triangle.brazier.item.BrazierIndicator;
import com.possible_triangle.brazier.network.BrazierNetwork;
import com.possible_triangle.brazier.network.SyncConfigMessage;
import me.shedaniel.architectury.event.events.LifecycleEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.architectury.event.events.TextureStitchEvent;
import me.shedaniel.architectury.event.events.TickEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public class Brazier {

    public static final String MOD_ID = "brazier";

    private static ConfigHolder<ServerConfig> LOCAL_SERVER_CONFIG;
    private static ServerConfig SYNCED_SERVER_CONFIG;
    public static ConfigHolder<ClientConfig> CLIENT_CONFIG;

    public static final Logger LOGGER = LogManager.getLogger();

    public static Supplier<ServerConfig> SERVER_CONFIG = () -> Optional.ofNullable(SYNCED_SERVER_CONFIG).orElseGet(LOCAL_SERVER_CONFIG);

    public static void setSyncedConfig(ServerConfig config) {
        SYNCED_SERVER_CONFIG = config;
    }

    public static void init() {

        LOCAL_SERVER_CONFIG = AutoConfig.register(ServerConfig.class, Toml4jConfigSerializer::new);
        CLIENT_CONFIG = AutoConfig.register(ClientConfig.class, Toml4jConfigSerializer::new);

        Content.init();
        BrazierNetwork.init();

        TickEvent.PLAYER_POST.register(BrazierIndicator::playerTick);
        PlayerEvent.PLAYER_JOIN.register(player ->
                BrazierNetwork.CHANNEL.sendToPlayer(player, new SyncConfigMessage(Brazier.SERVER_CONFIG.get()))
        );

        if (Boolean.parseBoolean(System.getenv("MC_TESTING"))) {
            LifecycleEvent.SERVER_STARTED.register(server -> {
                LOGGER.info("Detected testing environment, stopping server");
                server.getWorldData().overworldData().getScheduledEvents().schedule("shutdown", 100L, (object, timerQueue, l) -> {
                    Runtime.getRuntime().halt(0);
                });
            });
        }

    }

    public static void clientInit() {
        TextureStitchEvent.PRE.register(BrazierRenderer::atlasStitch);
        TextureStitchEvent.POST.register(BrazierRenderer::atlasStitch);
    }

    public static void setup() {
        Content.setup();
    }

    public static void clientSetup() {
        Content.clientSetup();
    }

}
