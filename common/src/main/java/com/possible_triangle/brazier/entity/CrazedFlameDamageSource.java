package com.possible_triangle.brazier.entity;

import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

public class CrazedFlameDamageSource extends IndirectEntityDamageSource {

    public CrazedFlameDamageSource(Entity entity, Entity caster) {
        super("inFire", entity, caster);
    }

    @Override
    public boolean isFire() {
        return true;
    }
}
