package com.cogworks.cogcrasher.entity.ai;

import com.cogworks.cogcrasher.entity.MalformedEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MalformedBehaviorGoal extends Goal {

    private final MalformedEntity malformed;
    private Player target;
    private int attackCooldown;

    public MalformedBehaviorGoal(MalformedEntity malformed) {
        this.malformed = malformed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = findTarget();
        return this.target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null
                && this.target.isAlive()
                && this.malformed.distanceToSqr(this.target) <= 1024.0D;
    }

    @Override
    public void start() {
        this.attackCooldown = 0;
    }

    @Override
    public void stop() {
        this.target = null;
        this.malformed.getNavigation().stop();
        this.malformed.setDeltaMovement(
                this.malformed.getDeltaMovement().x,
                this.malformed.getDeltaMovement().y,
                this.malformed.getDeltaMovement().z
        );
    }

    @Override
    public void tick() {
        if (this.target == null || !this.target.isAlive()) {
            return;
        }

        boolean beingWatched = isBeingWatched();

        if (beingWatched) {
            this.malformed.getNavigation().stop();
            this.malformed.setDeltaMovement(
                    0.0D,
                    this.malformed.getDeltaMovement().y,
                    0.0D
            );
            this.malformed.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            return;
        }

        this.malformed.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        this.malformed.getNavigation().moveTo(this.target, 0.16D);

        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }

        if (this.malformed.distanceToSqr(this.target) <= 3.0D && this.attackCooldown <= 0) {
            this.malformed.doHurtTarget(this.target); // TODO: replace with malform damage type
            this.attackCooldown = 20;
        }
    }

    private Player findTarget() {
        return this.malformed.level()
                .getEntitiesOfClass(
                        Player.class,
                        this.malformed.getBoundingBox().inflate(32.0D),
                        player -> player.isAlive()
                                && !player.isSpectator()
                                && !player.isCreative()
                )
                .stream()
                .min((a, b) -> Double.compare(
                        this.malformed.distanceToSqr(a),
                        this.malformed.distanceToSqr(b)
                ))
                .orElse(null);
    }

    private boolean isBeingWatched() {
        if (this.target == null) {
            return false;
        }

        if (this.malformed.distanceToSqr(this.target) > 144.0D) {
            return false;
        }

        Vec3 look = this.target.getLookAngle().normalize();
        Vec3 toMalformed = this.malformed.getEyePosition()
                .subtract(this.target.getEyePosition())
                .normalize();

        return look.dot(toMalformed) > 0.95D
                && this.target.hasLineOfSight(this.malformed);
    }
}