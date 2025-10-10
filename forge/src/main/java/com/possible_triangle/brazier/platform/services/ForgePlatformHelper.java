package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.BrazierForge;
import com.possible_triangle.multikulti.registrate.MultikultiRegistrate;
import com.tterrag.registrate.builders.EntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.ModList;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public MultikultiRegistrate<?> getRegistrate() {
        return BrazierForge.REGISTRATE.get();
    }

    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public <T extends Entity, S> NonNullFunction<EntityBuilder<T, S>, EntityBuilder<T, S>> sized(EntityDimensions fixed) {
        return builder -> builder.properties(props -> props.sized(fixed.width, fixed.height));
    }

    @Override
    public <T extends Entity, S> EntityBuilder<T, S> fireImmune(EntityBuilder<T, S> builder) {
        return builder.properties(EntityType.Builder::fireImmune);
    }

}
