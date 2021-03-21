package com.possible_triangle.brazier.entity.fabric;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class EntityAttributesImpl {

    public static void register(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder attributes) {
        FabricDefaultAttributeRegistry.register(type, attributes);
    }

}
