package com.cogworks.cogcrasher;

import com.cogworks.cogcrasher.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Cogcrasher.MODID)
public class Cogcrasher {
    public static final String MODID = "cogcrasher";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Cogcrasher(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(CogcrasherBlocks::register);
        modEventBus.addListener(ModItems::register);
        modEventBus.addListener(ModEntities::register);
        modEventBus.addListener(ModEntities::registerAttributes);
        modEventBus.addListener(ModBlockEntities::register);
        modEventBus.addListener(ModEffects::register);

        modEventBus.addListener(ModWorldgen::register);

        modEventBus.addListener(ModCreativeTabs::register);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.ANIMATED_BLACKSTONE);
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COGCRASHER LARRY CAN GET SCREWED");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Cogcrasher will be loaded :)");
    }
}
