package com.possible_triangle.brazier.entity.render;

import com.possible_triangle.brazier.entity.Crazed;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.util.ResourceLocation;

public class CrazedRender extends EvokerRenderer<Crazed> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/illager/evoker.png");

    public CrazedRender(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getEntityTexture(Crazed entity) {
        return TEXTURE;
    }
}
