package com.cogworks.cogcrasher;

import com.cogworks.cogcrasher.model.*;
import com.cogworks.cogcrasher.registry.*;
import com.cogworks.cogcrasher.renderers.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.*;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.*;

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
        event.registerLayerDefinition(ModModelLayers.MALFORMED, MalformedModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BLACKSTONE_GOLEM, BlackstoneGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.MALFORMED, MalformedRenderer::new);
    }
}
