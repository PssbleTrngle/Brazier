package com.possible_triangle.brazier.config;

import net.minecraft.dispenser.IPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;

import java.util.function.BiFunction;

public class DistanceHandler {

    @SuppressWarnings("unused")
    public enum Type {

        SPHERE((a, b) -> b.distanceSq(a, true)),
        CYLINDER((a, b) -> {
            Vector3d c = new Vector3d(a.getX(), b.getY(), a.getZ());
            return b.distanceSq(c, true);
        });

        private final BiFunction<Vector3d, BlockPos, Double> calc;

        Type(BiFunction<Vector3d, BlockPos, Double> calc) {
            this.calc = calc;
        }

    }

    public static double getDistance(Vector3d from, BlockPos to) {
        return BrazierConfig.SERVER.DISTANCE_CALC.get().calc.apply(from, to);
    }

    public static double getDistance(BlockPos from, BlockPos to) {
        return getDistance(new Vector3d(from.getX(), from.getY(), from.getZ()), to);
    }

}
