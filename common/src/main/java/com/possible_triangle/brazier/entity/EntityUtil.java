package com.possible_triangle.brazier.entity;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Function;

public class EntityUtil {

    @ExpectPlatform
    public static Packet<?> createSpawnPacket(Entity entity, String name) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <E extends Entity> EntityUtil.Builder<E> buildType(MobCategory category, EntityType.EntityFactory<E> factory) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void register(EntityType<? extends LivingEntity> type, AttributeSupplier.Builder attributes) {
        throw new AssertionError();
    }

    public interface Builder<E extends Entity> {

        Builder<E> size(float height, float width);

        Builder<E> fireImmune();

        default Builder<E> clientHandler(Function<ClientLevel, E> creator) {
            return this;
        }

        EntityType<E> build(String name);

    }

}
