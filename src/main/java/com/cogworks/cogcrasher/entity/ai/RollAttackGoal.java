package com.cogworks.cogcrasher.entity.ai;

import com.cogworks.cogcrasher.entity.BlackstoneGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class RollAttackGoal extends Goal {
    private final BlackstoneGolemEntity golem;
    private LivingEntity target;
    private Vec3 rollDirection;
    private Vec3 startPosition;
    private float lockedYaw;
    private int tickCounter = 0;

    public RollAttackGoal(BlackstoneGolemEntity golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.golem.isRollCooldownActive() || this.golem.isRolling()) {
            return false;
        }
        this.target = this.golem.getTarget();
        return this.target != null && this.target.isAlive() && this.golem.distanceToSqr(this.target) <= 225.0;
    }

    @Override
    public void start() {
        this.golem.setRolling(true);
        this.golem.setSprinting(true);
        this.startPosition = this.golem.position();
        this.tickCounter = 0;

        this.golem.rollStartAnimationState.start(this.golem.tickCount);

        Vec3 dir = this.target.position().subtract(this.golem.position());
        this.rollDirection = new Vec3(dir.x, 0, dir.z).normalize().scale(0.55);
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.golem.isRolling() || !this.golem.isAlive()) return false;

        if (this.golem.position().distanceTo(this.startPosition) >= 15.0) {
            return false;
        }

        if (this.golem.horizontalCollision && this.tickCounter > 2) {
            return false;
        }

        return this.tickCounter < 60; 
    }

    @Override
    public void tick() {
        this.tickCounter++;

        this.golem.setYRot(this.lockedYaw);
        this.golem.setYHeadRot(this.lockedYaw);
        this.golem.yBodyRot = this.lockedYaw;

        if (this.rollDirection != null) {
            this.golem.setDeltaMovement(
                    this.rollDirection.x,
                    this.golem.getDeltaMovement().y,
                    this.rollDirection.z
            );
        }

        if (this.target != null && this.golem.getBoundingBox().inflate(0.2).intersects(this.target.getBoundingBox())) {
            this.golem.doHurtTarget(this.target);
        }
    }

    @Override
    public void stop() {
        this.golem.setRolling(false);
        this.golem.setSprinting(false);
        this.golem.setDeltaMovement(Vec3.ZERO);
        this.golem.startRollCooldown(100); 
    }
}
