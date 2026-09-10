package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Cogcrasher.MODID);

    public static final DeferredBlock<Block> ANIMATED_BLACKSTONE = BLOCKS.registerSimpleBlock(
            "animated_blackstone",
            BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE)
    );
}
