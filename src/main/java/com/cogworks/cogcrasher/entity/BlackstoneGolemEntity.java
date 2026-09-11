package com.cogworks.cogcrasher.entity;

import com.cogworks.cogcrasher.entity.ai.RollAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BlackstoneGolemEntity extends Monster {
    // Defines a networked boolean parameter to track rolling safely across the server and client
    private static final EntityDataAccessor<Boolean> IS_ROLLING = SynchedEntityData.defineId(BlackstoneGolemEntity.class, EntityDataSerializers.BOOLEAN);

    private int rollCooldown = 0;

    public final AnimationState rollStartAnimationState = new AnimationState();
    public final AnimationState rollingLoopAnimationState = new AnimationState();

    public BlackstoneGolemEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        // Register your custom data watcher field with a default fallback state
        builder.define(IS_ROLLING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RollAttackGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, true));
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return this.isSprinting() ? EntityDimensions.scalable(2.8F, 2.8F) : EntityDimensions.scalable(2.8F, 3.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.rollCooldown > 0) {
            this.rollCooldown--;
        }
    }

    // Handles safe client-side animation state polling on every frame tick loop
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.isRolling()) {
                this.rollingLoopAnimationState.startIfStopped(this.tickCount);
            } else {
                this.rollingLoopAnimationState.stop();
            }
        }
    }

    public boolean isRolling() {
        return this.entityData.get(IS_ROLLING);
    }

    public void setRolling(boolean rolling) {
        this.entityData.set(IS_ROLLING, rolling);
    }

    public boolean isRollCooldownActive() { return this.rollCooldown > 0; }
    public void startRollCooldown(int ticks) { this.rollCooldown = ticks; }
}