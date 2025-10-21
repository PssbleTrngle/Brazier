package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.config.ConfigsService;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraftforge.fml.config.ModConfig;

public class FabricConfigs extends ConfigsService {

    @Override
    public void register() {
        ForgeConfigRegistry.INSTANCE.register(BrazierConstants.MOD_ID, ModConfig.Type.COMMON, serverConfig.getRight());
        ForgeConfigRegistry.INSTANCE.register(BrazierConstants.MOD_ID, ModConfig.Type.CLIENT, clientConfig.getRight());
    }

}
