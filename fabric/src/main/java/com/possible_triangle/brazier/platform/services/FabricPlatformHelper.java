package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.BrazierFabric;
import com.possible_triangle.multikulti.registrate.MultikultiRegistrate;
import com.tterrag.registrate.builders.EntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public MultikultiRegistrate<?> getRegistrate() {
        return BrazierFabric.REGISTRATE;
    }

    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    public <T extends Entity, S> NonNullFunction<EntityBuilder<T, S>, EntityBuilder<T, S>> sized(EntityDimensions fixed) {
        return builder -> builder.properties(props -> props.dimensions(fixed));
    }

    @Override
    public <T extends Entity, S> EntityBuilder<T, S> fireImmune(EntityBuilder<T, S> builder) {
        return builder.properties(FabricEntityTypeBuilder::fireImmune);
    }
}
