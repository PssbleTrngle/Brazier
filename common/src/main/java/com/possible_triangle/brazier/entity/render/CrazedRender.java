package com.possible_triangle.brazier.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.entity.Crazed;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.resources.ResourceLocation;

public class CrazedRender extends EvokerRenderer<Crazed> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Brazier.MOD_ID, "textures/entity/crazed.png");

    @Override
    public void render(Crazed entity, float yaw, float ticks, PoseStack matrizes, MultiBufferSource buffer, int light) {
        //entityModel.func_205062_a().showModel = true;
        super.render(entity, yaw, ticks, matrizes, buffer, light);
    }

    public CrazedRender(EntityRenderDispatcher manager) {
        super(manager);
        //model..showModel = true;
    }

    @Override
    public ResourceLocation getTextureLocation(Crazed entity) {
        return TEXTURE;
    }
}
