package com.possible_triangle.brazier.platform.fabric;

import com.possible_triangle.brazier.platform.PlatformRegistries;
import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PlatformRegistriesImpl {

    public static <E extends Entity> PlatformRegistries.Builder<E> createMob(MobCategory category, EntityType.EntityFactory<E> factory) {
        FabricEntityTypeBuilder<E> builder = FabricEntityTypeBuilder.create(category, factory);

        return new PlatformRegistries.Builder<E>() {

            @Nullable
            private AttributeSupplier.Builder attributes;

            @Override
            public PlatformRegistries.Builder<E> size(float height, float width) {
                builder.dimensions(EntityDimensions.fixed(width, height));
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
                var type = builder.build();
                if (attributes != null) {
                    FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) type, attributes);
                }
                return type;
            }
        };
    }

    public static <T extends CriterionTrigger<?>> Supplier<T> createCriteria(T criterion) {
        CriterionRegistry.register(criterion);
        return () -> criterion;
    }

}
