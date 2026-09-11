package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.block.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModBlocks {
    public static Block ANIMATED_BLACKSTONE;

    public static void register(RegisterEvent event) {
        event.register(BuiltInRegistries.BLOCK.key(), helper -> {
            ANIMATED_BLACKSTONE = new GolemBlock(BlockBehaviour
                    .Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE));
            helper.register(
                    ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "animated_blackstone"),
                    ANIMATED_BLACKSTONE
            );
        });
    }
}
