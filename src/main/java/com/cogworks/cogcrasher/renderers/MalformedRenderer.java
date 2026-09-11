package com.cogworks.cogcrasher.renderers;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.entity.MalformedEntity;
import com.cogworks.cogcrasher.model.MalformedModel;
import com.cogworks.cogcrasher.registry.ModModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MalformedRenderer extends MobRenderer<MalformedEntity, MalformedModel<MalformedEntity>> {

    public MalformedRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new MalformedModel<>(context.bakeLayer(ModModelLayers.MALFORMED)),
                0.5F
        );
    }

    @Override
    public ResourceLocation getTextureLocation(MalformedEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(
                Cogcrasher.MODID,
                "textures/entity/malformed.png"
        );
    }
}