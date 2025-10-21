package com.possible_triangle.brazier.logic;

import com.google.common.collect.Maps;
import com.possible_triangle.brazier.config.DistanceHandler;
import com.possible_triangle.brazier.index.BrazierBlocks;
import com.possible_triangle.brazier.index.BrazierTags;
import com.possible_triangle.brazier.platform.Services;
import java.util.HashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class BrazierLogic {

    private static final HashMap<ResourceKey<Level>, HashMap<BlockPos, Integer>> BRAZIERS = Maps.newHashMap();
    private static final float TOLERANCE = 0.03F;

    public static boolean isBorder(Vec3 pos, ResourceKey<Level> dimension) {
        synchronized (BRAZIERS) {
            return BRAZIERS.containsKey(dimension) && BRAZIERS.get(dimension).entrySet().stream().anyMatch(e -> {
                double dist = DistanceHandler.getDistance(pos, e.getKey());
                double minDist = Math.pow(e.getValue() - TOLERANCE, 2);
                double maxDist = Math.pow(e.getValue() + TOLERANCE, 2);
                return dist <= maxDist && dist >= minDist;
            });
        }
    }

    public static boolean inRange(BlockPos pos, ResourceKey<Level> dimension) {
        synchronized (BRAZIERS) {
            return BRAZIERS.containsKey(dimension) && BRAZIERS.get(dimension).entrySet().stream().anyMatch(e -> {
                if (!Services.CONFIGS.server().protectAbove() && e.getKey().getY() < pos.getY()) return false;
                double dist = DistanceHandler.getDistance(pos, e.getKey());
                int maxDist = e.getValue() * e.getValue();
                return dist <= maxDist;
            });
        }
    }

    public static void addBrazier(BlockPos pos, ResourceKey<Level> dimension, int range) {
        synchronized (BRAZIERS) {
            HashMap<BlockPos, Integer> map = BRAZIERS.getOrDefault(dimension, Maps.newHashMap());
            map.put(pos, range);
            BRAZIERS.put(dimension, map);
        }
    }

    public static void removeBrazier(BlockPos pos, ResourceKey<Level> dimension) {
        synchronized (BRAZIERS) {
            if(BRAZIERS.containsKey(dimension)) BRAZIERS.get(dimension).remove(pos);
        }
    }

    private static boolean prevents(Entity entity) {
        EntityType<?> type = entity.getType();
        return (
                entity instanceof Monster
                        && !type.is(BrazierTags.BRAZIER_WHITELIST)
        ) || type.is(BrazierTags.BRAZIER_BLACKLIST);
    }

    private static boolean prevents(MobSpawnType reason) {
        return switch (reason) {
            case CHUNK_GENERATION, NATURAL, PATROL -> true;
            default -> false;
        };
    }

    public static boolean prevents(Entity entity, LevelAccessor world, MobSpawnType reason) {
        BlockPos pos = entity.blockPosition();

        // Check for spawn powder
        if (Services.CONFIGS.server().enableSpawnPowder()) {
            var state = world.getBlockState(pos);
            if (BrazierBlocks.SPAWN_POWDER.filter(state::is).isPresent()) {
                return false;
            }
        }

        return prevents(reason) && prevents(entity) && BrazierLogic.inRange(pos, entity.level().dimension());
    }

}
