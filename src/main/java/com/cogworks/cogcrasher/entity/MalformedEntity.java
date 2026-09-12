package com.cogworks.cogcrasher.entity;

import com.cogworks.cogcrasher.entity.ai.MalformedBehaviorGoal;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class MalformedEntity extends Mob {

    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState invulnerableAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();

    public MalformedEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 10.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MalformedBehaviorGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (this.isDeadOrDying()) {
                this.walkAnimationState.stop();
                this.attackAnimationState.stop();
                this.invulnerableAnimationState.stop();

                this.deathAnimationState.startIfStopped(this.tickCount);
            } else {
                this.deathAnimationState.stop();

                if (this.getDeltaMovement().horizontalDistanceSqr() > 0.0001D) {
                    this.walkAnimationState.startIfStopped(this.tickCount);
                } else {
                    this.walkAnimationState.stop();
                }

                if (this.hurtTime > 0) {
                    this.invulnerableAnimationState.startIfStopped(this.tickCount);
                } else {
                    this.invulnerableAnimationState.stop();
                }
            }
        }
    }

    private void playCreakingSound(String sound) {
        if (!this.level().isClientSide()) {
            net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT
                    .getOptional(
                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                                    "minecraft",
                                    "entity.creaking." + sound
                            )
                    )
                    .ifPresent(event -> this.playSound(event, 1.0F, 1.0F));
        }
    }

    public void playAttackSound() {
        playCreakingSound("attack");
    }

    public void playDeathSound() {
        playCreakingSound("death");
    }

    public void playFreezeSound() {
        playCreakingSound("freeze");
    }

    public void playUnfreezeSound() {
        playCreakingSound("unfreeze");
    }

    public void playAttackAnimation() {
        if (this.level().isClientSide()) {
            this.attackAnimationState.start(this.tickCount);
        }
    }

    public boolean isChasing() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 0.0001D;
    }
}