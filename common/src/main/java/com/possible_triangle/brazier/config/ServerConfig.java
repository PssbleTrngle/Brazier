package com.possible_triangle.brazier.config;

import com.possible_triangle.brazier.ClientContent;
import me.shedaniel.autoconfig.example.ExampleConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ServerConfig implements IServerConfig {

    private final ForgeConfigSpec.BooleanValue jungleLoot, spawnCrazed;
    private final ForgeConfigSpec.DoubleValue crazedChance;
    private final ForgeConfigSpec.IntValue maxHeight, rangePerLevel, baseRange;
    private final ForgeConfigSpec.BooleanValue protectAbove, enableSpawnPowder, enableDecoration;
    private final ForgeConfigSpec.EnumValue<DistanceHandler.Type> distanceCalculator;

    public ServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("acquisition");

        jungleLoot = builder.define("jungleLoot", true);
        spawnCrazed = builder.define("spawnCrazed", true);
        crazedChance = builder.defineInRange("crazedChance", 0.6, 0.0, 1.0);

        builder.pop();
        builder.push("brazier");

        maxHeight = builder.defineInRange("maxHeight", 10, 1, Integer.MAX_VALUE);
        rangePerLevel = builder.defineInRange("rangePerLevel", 10, 1, Integer.MAX_VALUE);
        baseRange = builder.defineInRange("baseRange", 20, 1, Integer.MAX_VALUE);
        protectAbove = builder.define("protectAbove", false);
        distanceCalculator = builder.defineEnum("distanceCalculator", DistanceHandler.Type.CYLINDER);

        builder.pop();
        builder.push("content");

        enableSpawnPowder = builder.define("enableSpawnPowder", true);
        enableDecoration = builder.define("enableDecoration", true);
    }

    @Override
    public boolean injectJungleLoot() {
        return jungleLoot.get();
    }

    @Override
    public boolean spawnCrazed() {
        return spawnCrazed.get();
    }

    @Override
    public double crazedSpawnChance() {
        return crazedChance.get();
    }

    @Override
    public int maxHeight() {
        return maxHeight.get();
    }

    @Override
    public int rangePerLevel() {
        return rangePerLevel.get();
    }

    @Override
    public int baseRange() {
        return baseRange.get();
    }

    @Override
    public boolean protectAbove() {
        return protectAbove.get();
    }

    @Override
    public DistanceHandler.Type distanceCalculator() {
        return distanceCalculator.get();
    }


    @Override
    public boolean enableSpawnPowder() {
        return enableDecoration.get();
    }


    @Override
    public boolean enableDecoration() {
        return enableDecoration.get();
    }

}
