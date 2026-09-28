package com.possible_triangle.brazier.mixin;

import com.possible_triangle.brazier.index.BrazierEntities;
import com.possible_triangle.brazier.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WoodlandMansionPieces.WoodlandMansionPiece.class)
public class WoodlandMansionPiecesMixin {

    @Inject(at = @At("HEAD"), cancellable = true, method = "handleDataMarker(Ljava/lang/String;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)V")
    public void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor world, RandomSource rand, BoundingBox ssb, CallbackInfo callback) {
        var config = Services.CONFIGS.server();
        if (config.spawnCrazed() && function.equals("Mage") && config.crazedSpawnChance() > 0 && rand.nextDouble() <= config.crazedSpawnChance()) {
            var crazed = BrazierEntities.CRAZED.create(world.getLevel());
            if (crazed == null) return;

            crazed.setPersistenceRequired();
            crazed.moveTo(pos, 0.0F, 0.0F);
            crazed.finalizeSpawn(world, world.getCurrentDifficultyAt(crazed.blockPosition()), MobSpawnType.STRUCTURE, null);
            world.addFreshEntityWithPassengers(crazed);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            callback.cancel();
        }
    }

}
