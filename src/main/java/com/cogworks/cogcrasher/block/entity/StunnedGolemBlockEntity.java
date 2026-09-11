package com.cogworks.cogcrasher.block.entity;

import com.cogworks.cogcrasher.registry.ModBlockEntities;
import com.cogworks.cogcrasher.registry.ModBlocks;
import com.cogworks.cogcrasher.registry.ModEntities;
import com.cogworks.cogcrasher.entity.BlackstoneGolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StunnedGolemBlockEntity extends BlockEntity {
    private int timer = 100;
    private CompoundTag golemData = new CompoundTag();
    private BlockPos structureCenter = BlockPos.ZERO;

    public StunnedGolemBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STUNNED_GOLEM.get(), pos, state);
    }

    public void initialize(CompoundTag data, BlockPos center) {
        this.golemData = data;
        this.structureCenter = center;
        this.setChanged();
    }

    public void tick(Level level, BlockPos pos) {
        if (!pos.equals(structureCenter)) return;
        
        timer--;
        if (timer <= 0) {
            reform(level, golemData.getDouble("Health"));
        }
    }

    public void processBlockDestruction(Level level, float damageAmount) {
        if (level.getBlockEntity(structureCenter) instanceof StunnedGolemBlockEntity core) {
            double currentHealth = core.golemData.getDouble("Health");
            currentHealth -= damageAmount;
            core.golemData.putDouble("Health", currentHealth);

            if (currentHealth <= 0) {
                core.clearStructure(level);
            } else {
                core.reform(level, currentHealth);
            }
        }
    }

    private void clearStructure(Level level) {
        for (BlockPos p : BlockPos.betweenClosed(structureCenter.offset(-1, -1, -1), structureCenter.offset(1, 1, 1))) {
            if (level.getBlockState(p).is(ModBlocks.ANIMATED_BLACKSTONE)) {
                level.destroyBlock(p, true);
            }
        }
    }

    private void reform(Level level, double finalHealth) {
        for (BlockPos p : BlockPos.betweenClosed(structureCenter.offset(-1, -1, -1), structureCenter.offset(1, 1, 1))) {
            if (level.getBlockState(p).is(ModBlocks.ANIMATED_BLACKSTONE)) {
                level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            }
        }

        BlackstoneGolemEntity golem = ModEntities.BLACKSTONE_GOLEM.create(level);
        if (golem != null) {
            if (!golemData.isEmpty()) {
                golem.load(golemData);
            }
            golem.setHealth((float) finalHealth);
            golem.moveTo(structureCenter.getX() + 0.5, structureCenter.getY() - 1.0, structureCenter.getZ() + 0.5, level.random.nextFloat() * 360F, 0.0F);
            golem.setRolling(false);
            golem.setSprinting(false);
            golem.startRollCooldown(160);
            level.addFreshEntity(golem);
        }
    }
}
