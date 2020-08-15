package com.possible_triangle.brazier.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.entity.Crazed;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.util.ResourceLocation;

public class CrazedRender extends EvokerRenderer<Crazed> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Brazier.MODID, "textures/entity/crazed.png");

    @Override
    public void render(Crazed entity, float yaw, float ticks, MatrixStack matrizes, IRenderTypeBuffer buffer, int light) {
        entityModel.func_205062_a().showModel = true;
        super.render(entity, yaw, ticks, matrizes, buffer, light);
    }

    public CrazedRender(EntityRendererManager manager) {
        super(manager);
        entityModel.func_205062_a().showModel = true;
    }

    @Override
    public ResourceLocation getEntityTexture(Crazed entity) {
        return TEXTURE;
    }
}
