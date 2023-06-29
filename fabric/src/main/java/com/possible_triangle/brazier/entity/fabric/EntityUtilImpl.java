package com.possible_triangle.brazier.entity.fabric;

import com.possible_triangle.brazier.entity.EntityUtil;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.Nullable;

public class EntityUtilImpl {

    public static <E extends Entity> EntityUtil.Builder<E> buildType(MobCategory category, EntityType.EntityFactory<E> factory) {
        FabricEntityTypeBuilder<E> builder = FabricEntityTypeBuilder.create(category, factory);

        return new EntityUtil.Builder<E>() {

            @Nullable
            private AttributeSupplier.Builder attributes;

            @Override
            public EntityUtil.Builder<E> size(float height, float width) {
                builder.dimensions(EntityDimensions.fixed(width, height));
                return this;
            }

            @Override
            public EntityUtil.Builder<E> fireImmune() {
                builder.fireImmune();
                return this;
            }

            @Override
            public EntityUtil.Builder<E> attributes(AttributeSupplier.Builder attributes) {
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

}
