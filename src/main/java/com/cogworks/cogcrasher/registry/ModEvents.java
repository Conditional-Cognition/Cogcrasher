package com.cogworks.cogcrasher.registry;

import com.cogworks.cogcrasher.Cogcrasher;
import com.cogworks.cogcrasher.block.entity.StunnedGolemBlockEntity;
import com.cogworks.cogcrasher.entity.BlackstoneGolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = Cogcrasher.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void onGolemDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof BlackstoneGolemEntity golem) {
            if (event.getSource().getEntity() instanceof Player player) {
                boolean isPickaxe = player.getMainHandItem().getItem() instanceof PickaxeItem;
                boolean isCrit = player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater();

                if (golem.isRolling() && isPickaxe && isCrit) {
                    event.setCanceled(true);
                    Level level = golem.level();

                    Vec3 diff = golem.position().subtract(player.position());
                    Vec3 kbDir = new Vec3(diff.x, 0.0, diff.z).normalize();

                    Vec3 targetPos = golem.position().add(kbDir.scale(2.5));
                    BlockPos centerPos = BlockPos.containing(targetPos.x, golem.getY() + 1.0, targetPos.z);

                    boolean canPlace = true;
                    for (BlockPos p : BlockPos.betweenClosed(centerPos.offset(-1, -1, -1), centerPos.offset(1, 1, 1))) {
                        if (!level.getBlockState(p).canBeReplaced()) {
                            canPlace = false;
                            break;
                        }
                    }
                    if (!canPlace) centerPos = golem.blockPosition().above();

                    CompoundTag tag = new CompoundTag();
                    golem.saveWithoutId(tag);
                    golem.discard();

                    for (BlockPos p : BlockPos.betweenClosed(centerPos.offset(-1, -1, -1), centerPos.offset(1, 1, 1))) {
                        level.setBlock(p, ModBlocks.ANIMATED_BLACKSTONE.defaultBlockState(), 3);
                        if (level.getBlockEntity(p) instanceof StunnedGolemBlockEntity part) {
                            part.initialize(tag, centerPos);
                        }
                    }
                }
            }
        }
    }
}