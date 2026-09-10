package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Cogcrasher.MODID);
    public static final DeferredItem<BlockItem> ANIMATED_BLACKSTONE = ITEMS.registerSimpleBlockItem(
            "animated_blackstone",
            ModBlocks.ANIMATED_BLACKSTONE
    );
    public static final DeferredItem<Item> CREAKING_SWEET = ITEMS.registerSimpleItem("creaking_sweet", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
}
