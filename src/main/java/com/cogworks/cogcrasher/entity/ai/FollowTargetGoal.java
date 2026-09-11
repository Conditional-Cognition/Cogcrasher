package com.cogworks.cogcrasher.entity.ai;

import com.cogworks.cogcrasher.entity.BlackstoneGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FollowTargetGoal extends Goal {
    private final BlackstoneGolemEntity golem;
    private final double speed;

    public FollowTargetGoal(BlackstoneGolemEntity golem, double speed) {
        this.golem = golem;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.golem.getTarget();

        return target != null
                && target.isAlive()
                && !this.golem.isRolling();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.golem.getTarget();

        return target != null
                && target.isAlive()
                && !this.golem.isRolling();
    }

    @Override
    public void tick() {
        LivingEntity target = this.golem.getTarget();

        if (target == null) {
            return;
        }

        this.golem.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (this.golem.distanceToSqr(target) > 9.0D) {
            this.golem.getNavigation().moveTo(target, this.speed);
        } else {
            this.golem.getNavigation().stop();
        }
    }

    @Override
    public void stop() {
        this.golem.getNavigation().stop();
    }
}