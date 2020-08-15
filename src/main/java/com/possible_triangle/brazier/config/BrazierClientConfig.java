package com.possible_triangle.brazier.config;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collection;

public class BrazierServerConfig {

    public final ForgeConfigSpec.BooleanValue JUNGLE_LOOT;

    public final ForgeConfigSpec.IntValue MAX_HEIGHT;
    public final ForgeConfigSpec.IntValue RANGE_PER_LEVEL;
    public final ForgeConfigSpec.IntValue BASE_RANGE;

    public BrazierServerConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("These options will be synced to with all clients.").push("server");

        builder.comment("Options related to the brazier structure").push("brazier");

        MAX_HEIGHT = builder
                .comment("Should the living flame generate in jungle temple chests")
                .translation("config.brazier.maxHeight")
                .defineInRange("maxHeight", 10, 1, Integer.MAX_VALUE);

        RANGE_PER_LEVEL = builder
                .comment("Should the living flame generate in jungle temple chests")
                .translation("config.brazier.rangePerLevel")
                .defineInRange("rangePerLevel", 10, 0, Integer.MAX_VALUE);

        BASE_RANGE = builder
                .comment("Should the living flame generate in jungle temple chests")
                .translation("config.brazier.baseRange")
                .defineInRange("baseRange", 20, 0, Integer.MAX_VALUE);

        builder.pop().comment("Options related to the acquisition").push("acquisition");

        JUNGLE_LOOT = builder
                .comment("Should the living flame generate in jungle temple chests")
                .translation("config.brazier.jungleLoot")
                .define("jungleLoot", true);

    }

}
