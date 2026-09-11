package com.cogworks.cogcrasher.renderers;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.model.BlackstoneGolemModel;
import com.cogworks.cogcrasher.entity.BlackstoneGolemEntity;
import com.cogworks.cogcrasher.registry.ModModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BlackstoneGolemRenderer extends MobRenderer<BlackstoneGolemEntity, BlackstoneGolemModel<BlackstoneGolemEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            Cogcrasher.MODID, "textures/entity/blackstone_golem.png"
    );

    public BlackstoneGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new BlackstoneGolemModel<>(context.bakeLayer(ModModelLayers.BLACKSTONE_GOLEM_LAYER)), 1.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BlackstoneGolemEntity entity) {
        return TEXTURE;
    }
}
