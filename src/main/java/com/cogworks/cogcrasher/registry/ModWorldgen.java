package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.worldgen.MalformedMazeChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModWorldgen {
    public static void register(RegisterEvent event) {
        event.register(Registries.CHUNK_GENERATOR, helper -> {
            helper.register(
                    ResourceLocation.fromNamespaceAndPath(
                            Cogcrasher.MODID,
                            "malformed_maze"
                    ),
                    MalformedMazeChunkGenerator.CODEC
            );
        });
    }
}