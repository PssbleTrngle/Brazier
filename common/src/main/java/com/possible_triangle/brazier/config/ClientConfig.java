package com.possible_triangle.brazier.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig implements IClientConfig {

    private final ModConfigSpec.BooleanValue renderRunes;

    public ClientConfig(ModConfigSpec.Builder builder) {
        builder.push("client");

        renderRunes = builder.define("renderRunes", true);

        builder.pop();
    }

    @Override
    public boolean renderRunes() {
        return renderRunes.get();
    }

}
