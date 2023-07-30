package com.possible_triangle.brazier.config;

public record SyncedServerConfig(
        boolean injectJungleLoot,
        boolean spawnCrazed,
        double crazedSpawnChance,
        int maxHeight,
        int rangePerLevel,
        int baseRange,
        boolean protectAbove,
        DistanceHandler.Type distanceCalculator,
        boolean enableSpawnPowder,
        boolean enableDecoration

) implements IServerConfig {

}
