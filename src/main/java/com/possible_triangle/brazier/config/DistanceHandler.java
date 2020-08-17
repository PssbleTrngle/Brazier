package com.possible_triangle.brazier.config;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

import java.util.function.BiFunction;

public class DistanceHandler {

    @SuppressWarnings("unused")
    public enum Type {

        SPHERE(Vector3i::distanceSq),
        CYLINDER((a, b) -> {
            BlockPos c = new BlockPos(a.getX(), b.getY(), a.getZ());
            return b.distanceSq(c);
        });

        private final BiFunction<BlockPos, BlockPos, Double> calc;

        Type(BiFunction<BlockPos, BlockPos, Double> calc) {
            this.calc = calc;
        }

    }

    public static double getDistance(BlockPos from, BlockPos to) {
        return BrazierConfig.SERVER.DISTANCE_CALC.get().calc.apply(from, to);
    }

}
