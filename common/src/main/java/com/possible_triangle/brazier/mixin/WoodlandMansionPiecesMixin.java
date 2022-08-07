package com.possible_triangle.brazier.mixin;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.config.ServerConfig;
import com.possible_triangle.brazier.entity.Crazed;
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
        ServerConfig config = Brazier.serverConfig();
        if (config.SPAWN_CRAZED) Content.CRAZED.ifPresent(type -> {
            if (function.equals("Mage") && config.CRAZED_CHANCE > 0 && rand.nextDouble() <= config.CRAZED_CHANCE) {
                Crazed crazed = type.create(world.getLevel());
                assert crazed != null;
                crazed.setPersistenceRequired();
                crazed.moveTo(pos, 0.0F, 0.0F);
                crazed.finalizeSpawn(world, world.getCurrentDifficultyAt(crazed.blockPosition()), MobSpawnType.STRUCTURE, null, null);
                world.addFreshEntityWithPassengers(crazed);
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                callback.cancel();
            }
        });
    }

}
