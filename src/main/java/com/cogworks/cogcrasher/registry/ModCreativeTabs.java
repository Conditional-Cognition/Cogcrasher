package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModCreativeTabs {
    public static CreativeModeTab COGCRASHER_TAB;

    public static void register(RegisterEvent event) {
        event.register(BuiltInRegistries.CREATIVE_MODE_TAB.key(), helper -> {
            COGCRASHER_TAB = CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cogcrasher"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.CREAKING_SWEET.getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.CREAKING_SWEET);
                        output.accept(ModItems.ANIMATED_BLACKSTONE);
                    })
                    .build();
            helper.register(
                    ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "cogcrasher_tab"),
                    COGCRASHER_TAB
            );

        });
    }
}
