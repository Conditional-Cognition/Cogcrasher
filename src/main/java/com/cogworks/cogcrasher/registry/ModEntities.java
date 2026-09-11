package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.entity.BlackstoneGolemEntity;
import com.cogworks.cogcrasher.entity.MalformedEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class ModEntities {
    public static EntityType<BlackstoneGolemEntity> BLACKSTONE_GOLEM;
    public static EntityType<MalformedEntity> MALFORMED;

    public static void register(RegisterEvent event) {
        event.register(BuiltInRegistries.ENTITY_TYPE.key(), helper -> {
            BLACKSTONE_GOLEM = EntityType.Builder.of(
                    BlackstoneGolemEntity::new, MobCategory.MONSTER
                )
                .sized(2.8F, 3.0F) 
                .build("blackstone_golem");

            helper.register(
                ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "blackstone_golem"), 
                BLACKSTONE_GOLEM
            );
            MALFORMED = EntityType.Builder.of(
                            MalformedEntity::new, MobCategory.MONSTER
                    )
                    .sized(0.6F, 2.7F)
                    .build("malformed");

            helper.register(
                    ResourceLocation.fromNamespaceAndPath(Cogcrasher.MODID, "malformed"),
                    MALFORMED
            );
        });
    }


    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BLACKSTONE_GOLEM, BlackstoneGolemEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .build());
        event.put(ModEntities.MALFORMED, MalformedEntity.createAttributes().build());
    }
}
