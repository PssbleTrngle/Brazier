package com.possible_triangle.brazier.item;

import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.logic.BrazierLogic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.stream.Stream;

public class BrazierIndicator {

    private BrazierIndicator() {
    }

    public static void playerTick(Player player) {
        if (player.tickCount % 2 != 0) return;
        if(!(player.level() instanceof ServerLevel level)) return;

        Stream<ItemStack> items = Stream.of(player.getOffhandItem(), player.getMainHandItem());
        if (items.anyMatch(it -> it.is(Content.RANGE_INDICATOR))) {

            int step = 3;
            float radius = 5F;
            for (int i = -step; i <= step; i++) {
                float ry = i / (float) step;
                double y = player.position().y + ry * (radius / 2) + 1;
                for (float r = 0; r < radius; r += 0.2F) {
                    int degSteps = (int) (1 - r / radius) * 33 + 12;
                    for (int deg = 0; deg < 360; deg += degSteps) {
                        float rad = (float) ((deg + i + player.tickCount) / 180F * Math.PI);
                        double x = player.position().x + Math.sin(rad) * r;
                        double z = player.position().z + Math.cos(rad) * r;
                        if (BrazierLogic.isBorder(new Vec3(x, y, z), level.dimension())) {
                            level.sendParticles(Content.FLAME_PARTICLE.get(), x, y, z, 1, 0, 0.2, 0, 0.01);
                        }
                    }
                }
            }

        }
    }

}
