package com.cogworks.cogcrasher.block;

import com.cogworks.cogcrasher.block.entity.StunnedGolemBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class GolemBlock extends Block implements EntityBlock {
    public GolemBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StunnedGolemBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof StunnedGolemBlockEntity golemBe) {
                golemBe.tick(lvl, pos);
            }
        };
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-2, -2, -2), pos.offset(2, 2, 2))) {
                if (level.getBlockEntity(checkPos) instanceof StunnedGolemBlockEntity core) {
                    float flatDamage = 15.0F;
                    core.processBlockDestruction(level, flatDamage);
                    break;
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}