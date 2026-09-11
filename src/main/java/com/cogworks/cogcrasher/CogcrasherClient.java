package com.cogworks.cogcrasher;

import com.cogworks.cogcrasher.model.BlackstoneGolemModel;
import com.cogworks.cogcrasher.registry.ModEntities;
import com.cogworks.cogcrasher.registry.ModModelLayers;
import com.cogworks.cogcrasher.renderers.BlackstoneGolemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Cogcrasher.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Cogcrasher.MODID, value = Dist.CLIENT)
public class CogcrasherClient {
    public CogcrasherClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        Cogcrasher.LOGGER.info("HELLO FROM COGCRASHER CLIENT SETUP");
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.BLACKSTONE_GOLEM_LAYER, BlackstoneGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BLACKSTONE_GOLEM, BlackstoneGolemRenderer::new);
    }
}
