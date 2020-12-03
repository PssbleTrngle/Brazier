package com.possible_triangle.brazier.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BrazierServerConfig {

    public final ForgeConfigSpec.BooleanValue JUNGLE_LOOT;
    public final ForgeConfigSpec.BooleanValue SPAWN_CRAZED;
    public final ForgeConfigSpec.DoubleValue CRAZED_CHANCE;

    public final ForgeConfigSpec.IntValue MAX_HEIGHT;
    public final ForgeConfigSpec.IntValue RANGE_PER_LEVEL;
    public final ForgeConfigSpec.IntValue BASE_RANGE;
    public final ForgeConfigSpec.BooleanValue PROTECT_ABOVE;

    public final ForgeConfigSpec.BooleanValue SPAWN_POWDER;

    public final ForgeConfigSpec.EnumValue<DistanceHandler.Type> DISTANCE_CALC;

    public BrazierServerConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("These options will be synced to with all clients.").push("server");

        builder.comment("Options related to the brazier structure").push("brazier");

        MAX_HEIGHT = builder
                .comment("The maximum height a brazier will recognize")
                .translation("config.brazier.maxHeight")
                .defineInRange("maxHeight", 10, 1, Integer.MAX_VALUE);

        RANGE_PER_LEVEL = builder
                .comment("By how many blocks will the range grow per level (height)")
                .translation("config.brazier.rangePerLevel")
                .defineInRange("rangePerLevel", 10, 0, Integer.MAX_VALUE);

        BASE_RANGE = builder
                .comment("The base range. Base range + blocks per level = actual range")
                .translation("config.brazier.baseRange")
                .defineInRange("baseRange", 20, 0, Integer.MAX_VALUE);

        DISTANCE_CALC = builder
                .comment("How should the distance to the brazier be calculated?")
                .translation("config.brazier.distanceCalc")
                .defineEnum("distanceCalc", DistanceHandler.Type.CYLINDER);

        PROTECT_ABOVE = builder
                .comment("Should the brazier protect blocks above it too?")
                .translation("config.brazier.protectAbove")
                .define("protectAbove", false);

        SPAWN_POWDER = builder
                .comment("Enable to spawn powder block?")
                .translation("config.brazier.spawnPowder")
                .define("spawnPowder", true);

        builder.pop().comment("Options related to the acquisition of the living flame").push("acquisition");

        JUNGLE_LOOT = builder
                .comment("Should the living flame generate in jungle temple chests?")
                .translation("config.brazier.jungleLoot")
                .define("jungleLoot", true);

        SPAWN_CRAZED = builder
                .comment("Should crazed illagers spawn in generated woodland mansions?")
                .translation("config.brazier.spawnCrazed")
                .define("spawnCrazed", true);

        CRAZED_CHANCE = builder
                .comment("The chance a crazed will spawn instead of an evoker in a mansion")
                .translation("config.brazier.crazedChance")
                .defineInRange("crazedChance", 0.4D, 0D, 1D);

    }

}
