package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModItems {
    public static BlockItem ANIMATED_BLACKSTONE;
    public static Item CREAKING_SWEET;

    public static void register(RegisterEvent event) {
        event.register(BuiltInRegistries.ITEM.key(), helper -> {
            ANIMATED_BLACKSTONE = new BlockItem(CogcrasherBlocks.ANIMATED_BLACKSTONE, new Item.Properties());
            helper.register(
                    ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "animated_blackstone"),
                    ANIMATED_BLACKSTONE
            );
            CREAKING_SWEET = new Item(new Item.Properties().food(
                    new FoodProperties.Builder()
                            .alwaysEdible()
                            .nutrition(1)
                            .saturationModifier(2f)
                            .build()
            ));
            helper.register(
                    ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "creaking_sweet"),
                    CREAKING_SWEET
            );

        });
    }
}
