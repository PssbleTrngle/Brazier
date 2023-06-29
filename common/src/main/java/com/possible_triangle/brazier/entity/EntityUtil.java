package com.possible_triangle.brazier.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class EntityUtil {

    @ExpectPlatform
    public static <E extends Entity> EntityUtil.Builder<E> buildType(MobCategory category, EntityType.EntityFactory<E> factory) {
        throw new AssertionError();
    }

    public interface Builder<E extends Entity> {

        Builder<E> size(float height, float width);

        Builder<E> fireImmune();

        Builder<E> attributes(AttributeSupplier.Builder attributes);

        EntityType<E> build(String name);


    }

}
