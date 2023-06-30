package com.possible_triangle.brazier.entity.forge;

import com.possible_triangle.brazier.entity.EntityUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EntityUtilImpl {

    public static final Map<EntityType<? extends LivingEntity>,AttributeSupplier> ATTRIBUTES = new HashMap<>();

    public static <E extends Entity> EntityUtil.Builder<E> buildType(MobCategory category, EntityType.EntityFactory<E> factory) {
        EntityType.Builder<E> builder = EntityType.Builder.of(factory, category);
        return new EntityUtil.Builder<E>() {

            @Nullable
            private AttributeSupplier.Builder attributes;
            @Override
            public EntityUtil.Builder<E> size(float height, float width) {
                builder.sized(width, height);
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
                var type = builder.build(name);
                if(attributes != null) ATTRIBUTES.put((EntityType<? extends LivingEntity>) type, attributes.build());
                return type;
            }
        };
    }

    public static Packet<?> createSpawnPacket(Entity entity, String name) {
        return NetworkHooks.getEntitySpawningPacket(entity);
    }

}
