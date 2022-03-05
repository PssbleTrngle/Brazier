package com.possible_triangle.brazier.entity.forge;

import com.possible_triangle.brazier.entity.EntityUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashMap;
import java.util.Map;

public class EntityUtilImpl {

    public static final Map<EntityType<? extends LivingEntity>,AttributeSupplier> ATTRIBUTES = new HashMap<>();

    public static <E extends Entity> EntityUtil.Builder<E> buildType(MobCategory category, EntityType.EntityFactory<E> factory) {
        EntityType.Builder<E> builder = EntityType.Builder.of(factory, category);
        return new EntityUtil.Builder<E>() {
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
            public EntityType<E> build(String name) {
                return builder.build(name);
            }
        };
    }

    public static Packet<?> createSpawnPacket(Entity entity, String name) {
        return NetworkHooks.getEntitySpawningPacket(entity);
    }

    public static void register(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder attributes) {
        ATTRIBUTES.put(type, attributes.build());
    }

}
