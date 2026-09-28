package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.config.ConfigsService;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;

public class NeoForgeConfigs extends ConfigsService {

    @Override
    public void register() {
        var container = ModLoadingContext.get().getActiveContainer();
        container.registerConfig(ModConfig.Type.COMMON, serverConfig.getRight());
        container.registerConfig(ModConfig.Type.CLIENT, clientConfig.getRight());
    }

}
