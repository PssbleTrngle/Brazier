package com.possible_triangle.brazier;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrazierConstants {

    private BrazierConstants() {
    }

    public static final String MOD_ID = "brazier";

    public static ResourceLocation createId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static final Logger LOGGER = LogManager.getLogger();

}
