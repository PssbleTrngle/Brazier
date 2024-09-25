package com.possible_triangle.brazier.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig implements IClientConfig {

    private final ForgeConfigSpec.BooleanValue renderRunes;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("client");

        renderRunes = builder.define("renderRunes", true);

        builder.pop();
    }

    @Override
    public boolean renderRunes() {
        return renderRunes.get();
    }

}
