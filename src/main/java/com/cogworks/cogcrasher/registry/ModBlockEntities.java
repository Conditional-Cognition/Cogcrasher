package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.block.entity.StunnedGolemBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.RegisterEvent;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static Supplier<BlockEntityType<StunnedGolemBlockEntity>> STUNNED_GOLEM;

    public static void register(RegisterEvent event) {
        event.register(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), helper -> {
            BlockEntityType<StunnedGolemBlockEntity> type = BlockEntityType.Builder.of(
                StunnedGolemBlockEntity::new, CogcrasherBlocks.ANIMATED_BLACKSTONE
            ).build(null);
            
            STUNNED_GOLEM = () -> type;
            helper.register(ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "stunned_golem"), type);
        });
    }
}
