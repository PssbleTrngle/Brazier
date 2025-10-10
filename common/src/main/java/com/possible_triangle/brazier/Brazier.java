package com.possible_triangle.brazier;

import com.possible_triangle.brazier.platform.Services;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Brazier {

    private Brazier() {
    }

    public static final String MOD_ID = "brazier";

    public static ResourceLocation createId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        Content.init();

        Services.CONFIGS.register();
    }

}
