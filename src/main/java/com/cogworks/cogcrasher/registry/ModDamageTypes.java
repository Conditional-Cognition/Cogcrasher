package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> MALFORM = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "malform")
    );

    public static DamageSource malform(Level level, Entity source) {
        return new DamageSource(
                level.registryAccess()
                        .lookupOrThrow(Registries.DAMAGE_TYPE)
                        .getOrThrow(MALFORM),
                source
        );
    }
}