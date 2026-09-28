package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.config.ConfigsService;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ForgeConfigs extends ConfigsService {

    @Override
    public void register() {
        var context = ModLoadingContext.get();
        context.registerConfig(ModConfig.Type.COMMON, serverConfig.getRight());
        context.registerConfig(ModConfig.Type.CLIENT, clientConfig.getRight());
    }

}
