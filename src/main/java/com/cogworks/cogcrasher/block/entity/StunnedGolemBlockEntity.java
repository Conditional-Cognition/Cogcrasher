package com.cogworks.cogcrasher.block.entity;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.registry.ModBlockEntities;
import com.cogworks.cogcrasher.registry.CogcrasherBlocks;
import com.cogworks.cogcrasher.registry.ModEntities;
import com.cogworks.cogcrasher.entity.BlackstoneGolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

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
                if (level instanceof ServerLevel serverLevel) {
                    core.clearStructure(serverLevel);
                }
            } else {
                core.reform(level, currentHealth);
            }
        }
    }

    private void clearStructure(ServerLevel level) {
        for (BlockPos p : BlockPos.betweenClosed(
                structureCenter.offset(-1, -1, -1),
                structureCenter.offset(1, 1, 1))) {

            if (level.getBlockState(p).is(CogcrasherBlocks.ANIMATED_BLACKSTONE)) {
                level.destroyBlock(p, false);
            }
        }

        ResourceKey<LootTable> lootTableId = ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(
                        Cogcrasher.MODID,
                        "entities/blackstone_golem"
                )
        );

        LootParams lootParams = new LootParams.Builder(level)
                .withParameter(
                        LootContextParams.ORIGIN,
                        structureCenter.getCenter()
                )
                .create(LootContextParamSets.CHEST);

        level.getServer()
                .reloadableRegistries()
                .getLootTable(lootTableId)
                .getRandomItems(lootParams)
                .forEach(stack -> Block.popResource(level, structureCenter, stack));

        ExperienceOrb.award(
                level,
                structureCenter.getCenter(),
                50
        );
    }

    private void reform(Level level, double finalHealth) {
        for (BlockPos p : BlockPos.betweenClosed(structureCenter.offset(-1, -1, -1), structureCenter.offset(1, 1, 1))) {
            if (level.getBlockState(p).is(CogcrasherBlocks.ANIMATED_BLACKSTONE)) {
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
