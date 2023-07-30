package com.possible_triangle.brazier.config;

import com.possible_triangle.brazier.Brazier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;

public class DistanceHandler {

    public enum Type {

        SPHERE((a, b) -> b.distToCenterSqr(a.x, a.y, a.z)),
        CYLINDER((a, b) -> b.distToCenterSqr(a.x, b.getY(), a.z));

        private final BiFunction<Vec3, BlockPos, Double> calc;

        Type(BiFunction<Vec3, BlockPos, Double> calc) {
            this.calc = calc;
        }

    }

    public static double getDistance(Vec3 from, BlockPos to) {
        return Brazier.serverConfig().distanceCalculator().calc.apply(from, to);
    }

    public static double getDistance(BlockPos from, BlockPos to) {
        return getDistance(new Vec3(from.getX(), from.getY(), from.getZ()), to);
    }

}
