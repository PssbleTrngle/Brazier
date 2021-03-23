package com.possible_triangle.brazier.entity;

import com.possible_triangle.brazier.Content;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public class Crazed extends SpellcasterIllager {

    public static final double BUFF_RADIUS = 7;

    @SuppressWarnings("unused")
    public Crazed(Level world) {
        this(Content.CRAZED.get(), world);
    }

    public static void init(EntityType<Crazed> type) {
        EntityUtil.register(type, Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.6D)
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 24.0D)
        );
    }

    public Crazed(EntityType<? extends Crazed> type, Level world) {
        super(type, world);
    }

    /* TODO
    @Override
    public ItemStack getPickedResult( target) {
        return Content.CRAZED_SPAWN_EGG.map(ItemStack::new).orElse(ItemStack.EMPTY);
    }
    */

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SpellcasterCastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(5, new BuffSpellGoal());
        this.goalSelector.addGoal(6, new FlameSpellGoal());
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, Villager.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Guardian.class, false));
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.FIRECHARGE_USE;
    }

    @Override
    public void applyRaidBuffs(int wave, boolean something) {
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.EVOKER_HURT;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    class FlameSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
        private FlameSpellGoal() {
        }

        protected int getCastingTime() {
            return 40;
        }

        protected int getCastingInterval() {
            return 100;
        }

        protected void performSpellCasting() {
            LivingEntity target = Crazed.this.getTarget();
            if (target != null) spawnFlame(target.position().x, target.position().y, target.position().z);
        }

        private void spawnFlame(double x, double y, double z) {
            BlockPos blockpos = new BlockPos(x, y, z);
            if (!Crazed.this.level.isWaterAt(blockpos))
                Crazed.this.level.addFreshEntity(new CrazedFlame(Crazed.this.level, x, y + 0.4, z, Crazed.this));
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected IllagerSpell getSpell() {
            return IllagerSpell.FANGS;
        }
    }

    class BuffSpellGoal extends SpellcasterUseSpellGoal {
        private BuffSpellGoal() {
        }

        protected int getCastingTime() {
            return 10;
        }

        protected int getCastingInterval() {
            return 200;
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && getTargets().stream().anyMatch(e -> !e.hasEffect(MobEffects.FIRE_RESISTANCE));
        }

        private List<LivingEntity> getTargets() {
            return level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(BUFF_RADIUS), e ->
                    e.isAlive() && (EntityTypeTags.RAIDERS.contains(e.getType()) || e.isAlliedTo(Crazed.this))
            );
        }

        protected void performSpellCasting() {
            getTargets().forEach(this::buff);
        }

        private void buff(LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 10, 0));
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        protected IllagerSpell getSpell() {
            return IllagerSpell.FANGS;
        }
    }

    public static boolean canSpawnHere(EntityType<? extends Monster> type, ServerLevel world, MobSpawnType reason, BlockPos pos, Random random) {
        return checkAnyLightMonsterSpawnRules(type, world, reason, pos, random) && reason != MobSpawnType.NATURAL;
    }

}
