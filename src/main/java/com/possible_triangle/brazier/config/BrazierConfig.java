package com.possible_triangle.brazier.config;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.network.BrazierNetwork;
import com.possible_triangle.brazier.network.SyncConfigMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrazierConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final BrazierClientConfig CLIENT;
    public static final BrazierServerConfig SERVER;

    static {
        final Pair<BrazierClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BrazierClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static {
        final Pair<BrazierServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BrazierServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    @SubscribeEvent
    public static void configReload(ModConfig.Reloading event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                syncServerConfigs(player);
            }
        }
    }

    @SubscribeEvent
    public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        syncServerConfigs(event.getPlayer());
    }

    public static void syncServerConfigs(PlayerEntity player) {
        final Path config = FMLPaths.CONFIGDIR.get().resolve(Brazier.MODID + "-common.toml").toAbsolutePath();
        try {
            final byte[] configData = Files.readAllBytes(config);
            BrazierNetwork.sendTo(new SyncConfigMessage(configData), player);
        } catch (IOException ignored) {}
    }

}
