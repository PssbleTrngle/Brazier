package com.possible_triangle.brazier.logic;

import com.google.common.collect.Maps;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.config.DistanceHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

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
                if (!Brazier.SERVER_CONFIG.get().PROTECT_ABOVE && e.getKey().getY() < pos.getY()) return false;
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

}
