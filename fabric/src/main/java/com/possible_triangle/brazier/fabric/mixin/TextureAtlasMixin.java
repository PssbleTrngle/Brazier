package com.possible_triangle.brazier.fabric.mixin;

import com.possible_triangle.brazier.platform.services.FabricClientHelper;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlas.class)
public class TextureAtlasMixin {

    @Inject(
            method = "upload",
            at = @At("TAIL")
    )
    private void afterTexturesStitched(SpriteLoader.Preparations preparations, CallbackInfo ci) {
        var self = (TextureAtlas) (Object) this;
        FabricClientHelper.onAtlasReload(self);
    }

}
