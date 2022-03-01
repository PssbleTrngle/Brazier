package com.possible_triangle.brazier.mixin;

import com.possible_triangle.brazier.block.tile.BrazierTile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class WorldMixin {

    @Inject(at = @At("RETURN"), method = "setBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;)V")
    public void addBlockEntity(BlockEntity tile, CallbackInfo info) {
        if (tile instanceof BrazierTile brazier) brazier.onLoad();
    }

}
