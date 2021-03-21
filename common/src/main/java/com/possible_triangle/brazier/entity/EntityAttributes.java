package com.possible_triangle.brazier.entity;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class EntityAttributes {

    @ExpectPlatform
    public static void register(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder attributes) {
        throw new AssertionError();
    }

}
