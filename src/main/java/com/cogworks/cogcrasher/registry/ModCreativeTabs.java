package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Cogcrasher.MODID);
    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COGCRASHER_TAB = CREATIVE_MODE_TABS.register("cogcrasher_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cogcrasher"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.CREAKING_SWEET.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.CREAKING_SWEET.get());
            }).build());
}