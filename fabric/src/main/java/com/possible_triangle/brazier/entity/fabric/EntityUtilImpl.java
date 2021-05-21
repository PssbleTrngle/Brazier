package com.possible_triangle.brazier.entity.fabric;

import com.possible_triangle.brazier.entity.EntityUtil;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class EntityUtilImpl {

    public static Packet<?> createSpawnPacket(Entity entity, String name) {

        CustomEntityNetworking.notifyClients(entity, name);

        if (entity instanceof AbstractHurtingProjectile) {
            Entity owner = ((AbstractHurtingProjectile) entity).getOwner();
            return new ClientboundAddEntityPacket(entity, owner == null ? entity.getId() : owner.getId());
        } else {
            return new ClientboundAddEntityPacket(entity, entity.getId());
        }
    }

    public static <E extends Entity> EntityUtil.Builder<E> buildType(MobCategory category, EntityType.EntityFactory<E> factory) {
        FabricEntityTypeBuilder<E> builder = FabricEntityTypeBuilder.create(category, factory);

        return new EntityUtil.Builder<E>() {
            private Function<Level, E> creator;

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
            public EntityUtil.Builder<E> clientHandler(Function<Level, E> creator) {
                this.creator = creator;
                return this;
            }

            @Override
            public EntityType<E> build(String name) {
                if(creator != null) CustomEntityNetworking.registerHandler(name, creator);
                return builder.build();
            }
        };
    }

    public static void register(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder attributes) {
        FabricDefaultAttributeRegistry.register(type, attributes);
    }

}
