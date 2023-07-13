package com.possible_triangle.brazier.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public class PlatformRegistries {

    @ExpectPlatform
    public static <E extends Entity> PlatformRegistries.Builder<E> createMob(MobCategory category, EntityType.EntityFactory<E> factory) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends CriterionTrigger<?>> Supplier<T> createCriteria(T criterion) {
        throw new AssertionError();
    }

    public interface Builder<E extends Entity> {

        Builder<E> size(float height, float width);

        Builder<E> fireImmune();

        Builder<E> attributes(AttributeSupplier.Builder attributes);

        EntityType<E> build(String name);


    }

}
