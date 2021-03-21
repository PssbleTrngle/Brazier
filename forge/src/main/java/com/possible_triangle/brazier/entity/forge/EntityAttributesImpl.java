package com.possible_triangle.brazier.entity.forge;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;

public class EntityAttributesImpl {

    public static void register(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder attributes) {
        DefaultAttributes.put(type, attributes.build());
    }

}
