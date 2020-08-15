package com.possible_triangle.brazier.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BrazierClientConfig {

    public final ForgeConfigSpec.BooleanValue RENDER_RUNES;

    public BrazierClientConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("These options only affect the client").push("client");

        RENDER_RUNES = builder
                .comment("Should brazier structure render runes?")
                .translation("config.brazier.renderRunes")
                .define("renderRunes", true);

    }

}
