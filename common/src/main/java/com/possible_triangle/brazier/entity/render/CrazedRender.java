package com.possible_triangle.brazier.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.entity.Crazed;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CrazedRender extends EvokerRenderer<Crazed> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Brazier.MOD_ID, "textures/entity/crazed.png");

    @Override
    public void render(@NotNull Crazed entity, float yaw, float ticks, @NotNull PoseStack matrizes, @NotNull MultiBufferSource buffer, int light) {
        model.getHat().visible = true;
        super.render(entity, yaw, ticks, matrizes, buffer, light);
    }

    public CrazedRender(EntityRendererProvider.Context context) {
        super(context);
        model.getHat().visible = true;
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull Crazed entity) {
        return TEXTURE;
    }
}
