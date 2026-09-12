package com.cogworks.cogcrasher.entity.ai;

import com.cogworks.cogcrasher.entity.MalformedEntity;
import com.cogworks.cogcrasher.registry.ModDamageTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class MalformedBehaviorGoal extends Goal {

    private final MalformedEntity malformed;
    private Player target;
    private int attackCooldown;
    private boolean wasBeingWatched;

    public MalformedBehaviorGoal(MalformedEntity malformed) {
        this.malformed = malformed;
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
    }

    @Override
    public void tick() {
        if (this.target == null || !this.target.isAlive()) {
            return;
        }

        this.malformed.getLookControl().setLookAt(
                this.target,
                30.0F,
                30.0F
        );

        boolean beingWatched = isBeingWatched();

        if (beingWatched) {
            if (!this.wasBeingWatched) {
                this.malformed.playFreezeSound();
            }

            this.malformed.getNavigation().stop();
        } else {
            if (this.wasBeingWatched) {
                this.malformed.playUnfreezeSound();
            }
        }

        this.wasBeingWatched = beingWatched;

        if (beingWatched) {
            return;
        }

        double distance = this.malformed.distanceToSqr(this.target);

        if (distance > 4.0D) {
            this.malformed.getNavigation().moveTo(this.target, 2D);
        } else {
            this.malformed.getNavigation().stop();

            if (this.attackCooldown > 0) {
                this.attackCooldown--;
            }

            if (this.attackCooldown <= 0) {
                this.target.hurt(
                        this.malformed.damageSources().source(
                                ModDamageTypes.MALFORM
                        ),
                        (float) this.malformed.getAttributeValue(Attributes.ATTACK_DAMAGE)
                );
                this.attackCooldown = 20;
                this.malformed.playAttackAnimation();
                this.malformed.playAttackSound();
            }
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