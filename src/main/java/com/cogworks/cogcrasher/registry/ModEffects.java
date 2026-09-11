package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.effect.PetrificationEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModEffects {

    public static final ResourceKey<MobEffect> PETRIFICATION_KEY =
            ResourceKey.create(
                    Registries.MOB_EFFECT,
                    ResourceLocation.fromNamespaceAndPath(
                            Cogcrasher.MODID,
                            "petrification"
                    )
            );

    public static MobEffect PETRIFICATION;
    public static Holder<MobEffect> PETRIFICATION_HOLDER;

    public static void register(RegisterEvent event) {
        event.register(BuiltInRegistries.MOB_EFFECT.key(), helper -> {
            PETRIFICATION = new PetrificationEffect();

            helper.register(
                    PETRIFICATION_KEY.location(),
                    PETRIFICATION
            );

            PETRIFICATION_HOLDER =
                    BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(PETRIFICATION_KEY);
        });
    }
}