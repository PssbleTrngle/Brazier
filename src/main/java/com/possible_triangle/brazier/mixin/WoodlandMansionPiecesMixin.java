package com.possible_triangle.brazier.mixin;

import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.config.BrazierConfig;
import com.possible_triangle.brazier.entity.Crazed;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.WoodlandMansionPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WoodlandMansionPieces.MansionTemplate.class)
public class WoodlandMansionPiecesMixin {

    @Inject(at = @At("HEAD"), cancellable = true, method = "Lnet/minecraft/world/gen/feature/structure/WoodlandMansionPieces$MansionTemplate;handleDataMarker(Ljava/lang/String;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/IWorld;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;)V")
    public void handleDataMarker(String function, BlockPos pos, IWorld world, Random rand, MutableBoundingBox sbb, CallbackInfo callback) {
        if (BrazierConfig.SERVER.SPAWN_CRAZED.get()) Content.CRAZED.ifPresent(type -> {
            if (function.equals("Mage") && rand.nextInt(3) == 0) {
                Crazed crazed = type.create(world.getWorld());
                assert crazed != null;
                crazed.enablePersistence();
                crazed.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
                crazed.onInitialSpawn(world, world.getDifficultyForLocation(crazed.func_233580_cy_()), SpawnReason.STRUCTURE, null, null);
                world.addEntity(crazed);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                callback.cancel();
            }
        });
    }

}
