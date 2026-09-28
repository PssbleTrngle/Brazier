package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.config.ConfigsService;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.neoforged.fml.config.ModConfig;

public class FabricConfigs extends ConfigsService {

    @Override
    public void register() {
        NeoForgeConfigRegistry.INSTANCE.register(BrazierConstants.MOD_ID, ModConfig.Type.COMMON, serverConfig.getRight());
        NeoForgeConfigRegistry.INSTANCE.register(BrazierConstants.MOD_ID, ModConfig.Type.CLIENT, clientConfig.getRight());
    }

}
