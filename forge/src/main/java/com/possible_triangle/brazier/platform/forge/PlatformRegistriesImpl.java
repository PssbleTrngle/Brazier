package com.possible_triangle.brazier.platform.forge;

import com.possible_triangle.brazier.platform.PlatformRegistries;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PlatformRegistriesImpl {

    public static final Map<EntityType<? extends LivingEntity>,AttributeSupplier> ATTRIBUTES = new HashMap<>();

    public static <E extends Entity> PlatformRegistries.Builder<E> createMob(MobCategory category, EntityType.EntityFactory<E> factory) {
        EntityType.Builder<E> builder = EntityType.Builder.of(factory, category);
        return new PlatformRegistries.Builder<E>() {

            @Nullable
            private AttributeSupplier.Builder attributes;
            @Override
            public PlatformRegistries.Builder<E> size(float height, float width) {
                builder.sized(width, height);
                return this;
            }

            @Override
            public PlatformRegistries.Builder<E> fireImmune() {
                builder.fireImmune();
                return this;
            }

            @Override
            public PlatformRegistries.Builder<E> attributes(AttributeSupplier.Builder attributes) {
                this.attributes = attributes;
                return this;
            }

            @Override
            public EntityType<E> build(String name) {
                var type = builder.build(name);
                if(attributes != null) ATTRIBUTES.put((EntityType<? extends LivingEntity>) type, attributes.build());
                return type;
            }
        };
    }

    public static <T extends CriterionTrigger<?>> Supplier<T> createCriteria(T criterion) {
        CriteriaTriggers.register(criterion);
        return () -> criterion;
    }

}
