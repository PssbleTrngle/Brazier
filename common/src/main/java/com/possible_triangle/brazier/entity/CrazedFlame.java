package com.possible_triangle.brazier.entity;

import com.possible_triangle.brazier.Content;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import org.jetbrains.annotations.NotNull;

public class CrazedFlame extends AbstractHurtingProjectile {

    private static final int INITIAL_LIFE = 20 * 4;

    private int life = INITIAL_LIFE;

    public CrazedFlame(Level world, LivingEntity caster) {
        super(Content.CRAZED_FLAME.get(), caster, 0, 0, 0, world);
    }

    public CrazedFlame(Level world) {
        this(Content.CRAZED_FLAME.get(), world);
    }

    public CrazedFlame(EntityType<? extends AbstractHurtingProjectile> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        var level = level();

        --this.life;
        if (level.isClientSide && this.life % 4 == 0) {
            for (int i = 0; i < 2; ++i) {
                double x = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * this.getBbWidth() * 0.5D;
                double y = this.getY() - 0.4;
                double z = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * this.getBbWidth() * 0.5D;
                double dx = (this.random.nextDouble() * 2.0D - 1.0D) * 0.05D;
                double dy = 0.02D + this.random.nextDouble() * 0.05D;
                double dz = (this.random.nextDouble() * 2.0D - 1.0D) * 0.05D;
                level.addParticle(Content.FLAME_PARTICLE.get(), x, y + 1.0D, z, dx, dy, dz);
            }
        } else {
            if (this.life <= 0) remove(RemovalReason.KILLED);

            if (INITIAL_LIFE - 20 > life && life % 5 == 0) {
                level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.2D, 0.2D, 0.2D)).forEach(this::damage);
                BlockPos pos = this.blockPosition();
                if (level.getBlockState(pos).isAir()) {
                    level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
                }
            }
        }
    }

    private void damage(LivingEntity target) {
        Entity caster = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && target != caster) {
            if (caster == null) {
                target.hurt(level().damageSources().inFire(), 6.0F);
            } else if (!caster.isAlliedTo(target)) {
                target.hurt(level().damageSources().inFire(), 6.0F);
                // TODO target.hurt(new CrazedFlameDamageSource(this, caster).setMagic(), 6.0F);
            }
        }
    }

    /*
    @Override
    public Packet<?> getAddEntityPacket() {
        return EntityUtil.createSpawnPacket(this, "crazed_flame");
    }
    */

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("life")) this.life = compound.getInt("life");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("life", life);
    }

}
