package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation BLACKSTONE_GOLEM_LAYER = new ModelLayerLocation(
        ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "blackstone_golem"), "main"
    );
    public static final ModelLayerLocation MALFORMED = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "malformed"), "main"
    );
}
