package com.possible_triangle.brazier.entity;

import com.possible_triangle.brazier.Content;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class CrazedFlame extends Entity {

    private static final int INITIAL_LIFE = 20 * 4;

    private LivingEntity caster;
    private UUID casterUuid;
    private int life = INITIAL_LIFE;
    private boolean sentSpikeEvent;

    public CrazedFlame(World world, double x, double y, double z, LivingEntity caster) {
        this(world);
        this.setCaster(caster);
        this.setPosition(x, y, z);
    }

    @SuppressWarnings("unused")
    public CrazedFlame(World world) {
        this(Content.CRAZED_FLAME.get(), world);
    }

    public CrazedFlame(EntityType<? extends CrazedFlame> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        --this.life;
        if (world.isRemote) {
            if (this.life % 4 == 0) {
                for (int i = 0; i < 2; ++i) {
                    double x = this.getPosX() + (this.rand.nextDouble() * 2.0D - 1.0D) * (double) this.getWidth() * 0.5D;
                    double y = this.getPosY() - 0.4;
                    double z = this.getPosZ() + (this.rand.nextDouble() * 2.0D - 1.0D) * (double) this.getWidth() * 0.5D;
                    double dx = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.05D;
                    double dy = 0.02D + this.rand.nextDouble() * 0.05D;
                    double dz = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.05D;
                    this.world.addParticle(Content.FLAME_PARTICLE.get(), x, y + 1.0D, z, dx, dy, dz);
                }
            }
        } else {
            if (this.life <= 0) remove();

            if (INITIAL_LIFE - 20 > life && life % 5 == 0) {
                world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(0.2D, 0.2D, 0.2D)).forEach(this::damage);
                BlockPos pos = new BlockPos(this.getPositionVec());
                if(world.getBlockState(pos).isAir(world, pos) && world.getBlockState(pos.down()).isFlammable(world, pos.down(), Direction.UP))
                    world.setBlockState(pos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    private void damage(LivingEntity target) {
        LivingEntity caster = this.getCaster();
        if (target.isAlive() && !target.isInvulnerable() && target != caster) {
            if (caster == null) {
                target.attackEntityFrom(DamageSource.IN_FIRE, 6.0F);
            } else if (!caster.isOnSameTeam(target)) {
                target.attackEntityFrom(new IndirectEntityDamageSource("inFire", this, caster).setFireDamage(), 6.0F);
            }
        }
    }

    public void setCaster(@Nullable LivingEntity caster) {
        this.caster = caster;
        this.casterUuid = caster == null ? null : caster.getUniqueID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity) entity;
            }
        }

        return this.caster;
    }

    @Override
    protected void registerData() {
    }

    protected void readAdditional(CompoundNBT compound) {
        if (compound.hasUniqueId("owner")) {
            this.casterUuid = compound.getUniqueId("owner");
        }
        if (compound.contains("life")) this.life = compound.getInt("life");
    }

    protected void writeAdditional(CompoundNBT compound) {
        if (this.casterUuid != null) {
            compound.putUniqueId("owner", this.casterUuid);
        }
        compound.putInt("life", life);
    }

    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
