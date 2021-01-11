package com.possible_triangle.brazier.entity;

import com.possible_triangle.brazier.Content;
import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class CrazedFlame extends Entity {

    private static final int INITIAL_LIFE = 20 * 4;

    private LivingEntity caster;
    private UUID casterUuid;
    private int life = INITIAL_LIFE;

    public CrazedFlame(Level world, double x, double y, double z, LivingEntity caster) {
        this(world);
        this.setCaster(caster);
        this.setPos(x, y, z);
    }

    @SuppressWarnings("unused")
    public CrazedFlame(Level world) {
        this(Content.CRAZED_FLAME.get(), world);
    }

    public CrazedFlame(EntityType<? extends CrazedFlame> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        --this.life;
        if (level.isClientSide) {
            if (this.life % 4 == 0) {
                for (int i = 0; i < 2; ++i) {
                    double x = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D;
                    double y = this.getY() - 0.4;
                    double z = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getBbWidth() * 0.5D;
                    double dx = (this.random.nextDouble() * 2.0D - 1.0D) * 0.05D;
                    double dy = 0.02D + this.random.nextDouble() * 0.05D;
                    double dz = (this.random.nextDouble() * 2.0D - 1.0D) * 0.05D;
                    level.addParticle(Content.FLAME_PARTICLE.get(), x, y + 1.0D, z, dx, dy, dz);
                }
            }
        } else {
            if (this.life <= 0) remove();

            if (INITIAL_LIFE - 20 > life && life % 5 == 0) {
                level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.2D, 0.2D, 0.2D)).forEach(this::damage);
                BlockPos pos = this.blockPosition();
                if(level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).getMaterial().isFlammable())
                    level.setBlockAndUpdate(pos,  BaseFireBlock.getState(level, pos));
            }
        }
    }

    private void damage(LivingEntity target) {
        LivingEntity caster = this.getCaster();
        if (target.isAlive() && !target.isInvulnerable() && target != caster) {
            if (caster == null) {
                target.hurt(DamageSource.IN_FIRE, 6.0F);
            } else if (!caster.isAlliedTo(target)) {
                target.hurt(new CrazedFlameDamageSource(this, caster).setMagic(), 6.0F);
            }
        }
    }

    public void setCaster(@Nullable LivingEntity caster) {
        this.caster = caster;
        this.casterUuid = caster == null ? null : caster.getUUID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level).getEntity(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity) entity;
            }
        }

        return this.caster;
    }

    @Override
    protected void defineSynchedData() {
    }


    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("owner")) {
            this.casterUuid = compound.getUUID("owner");
        }
        if (compound.contains("life")) this.life = compound.getInt("life");
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.casterUuid != null) {
            compound.putUUID("owner", this.casterUuid);
        }
        compound.putInt("life", life);
    }

    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, caster == null ? 0 : caster.getId());
    }

}
