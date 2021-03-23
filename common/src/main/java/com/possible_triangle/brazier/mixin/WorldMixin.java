package com.possible_triangle.brazier.mixin;

import com.possible_triangle.brazier.block.tile.BaseTile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class WorldMixin {

    @Inject(at = @At("RETURN"), method = "addBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;)Z")
    public void addBlockEntity(BlockEntity tile, CallbackInfoReturnable<Boolean> info) {
        if(tile instanceof BaseTile) ((BaseTile) tile).onLoad();
    }

}
