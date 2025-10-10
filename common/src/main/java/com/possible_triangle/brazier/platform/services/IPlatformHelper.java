package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.multikulti.registrate.MultikultiRegistrate;
import com.tterrag.registrate.builders.EntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;

public interface IPlatformHelper {

    MultikultiRegistrate<?> getRegistrate();

    <T extends Entity, S> NonNullFunction<EntityBuilder<T, S>, EntityBuilder<T, S>> sized(EntityDimensions fixed);

    <T extends Entity, S> EntityBuilder<T, S> fireImmune(EntityBuilder<T, S> builder);

    boolean isModLoaded(String modid);
}
