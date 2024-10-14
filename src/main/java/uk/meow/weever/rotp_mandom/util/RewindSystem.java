package uk.meow.weever.rotp_mandom.util;

import com.github.standobyte.jojo.util.mc.MCUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.meow.weever.rotp_mandom.data.entity.*;
import uk.meow.weever.rotp_mandom.data.world.*;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.config.*;
import uk.meow.weever.rotp_mandom.init.InitItems;
import uk.meow.weever.rotp_mandom.item.RingoClock;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID)
public class RewindSystem {
    private static Set<PlayerEntity> zePizdecForBlockInteraction = new HashSet<>();
    // public static Queue<BlockData> saveBlocks(PlayerEntity player, int range) {
    //     Queue<BlockData> blockDataList = new LinkedList<>();
    //     for (BlockPos pos : BlockPos.betweenClosed(
    //             player.blockPosition().getX() - range, 0, player.blockPosition().getZ() - range,
    //             player.blockPosition().getX() + range, 255, player.blockPosition().getZ() + range)) {

    //         BlockState blockState = player.level.getBlockState(pos);
    //         blockDataList.add(new BlockData(pos, blockState));
    //     }
    //     return blockDataList; 
    // }

    public static void saveData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            CapabilityUtil.removeRewindData(player);

            Queue<LivingEntityData> livingEntitiesData = new LinkedList<>();
            Queue<ProjectileData> projectilesData = new LinkedList<>();
            Queue<ItemData> itemsData = new LinkedList<>();
            WorldData worldData = null;
            Queue<BlockData> blockData = new LinkedList<>();

            if (TPARConfig.getSaveWorld(player.level.isClientSide())) {
                worldData = WorldData.saveWorldData(player.level);
            }
            if (TPARConfig.getSaveEntities(player.level.isClientSide())) {
                List<LivingEntity> livingEntities = new ArrayList<>();
                livingEntities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, entity -> (!(entity instanceof ArmorStandEntity))));
                livingEntities.add(player);
                livingEntities.forEach(entity -> {
                    livingEntitiesData.add(LivingEntityData.saveLivingEntityData(entity));
                });
            }
            if (TPARConfig.getSaveItems(player.level.isClientSide())) {
                List<ItemEntity> items = new ArrayList<>();
                items.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, null));
                items.forEach(entity -> {
                    itemsData.add(ItemData.saveItemData(entity));
                });
            }
            if (TPARConfig.getSaveProjectiles(player.level.isClientSide())) {
                List<ProjectileEntity> projectiles = new ArrayList<>();
                projectiles.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, null));
                projectiles.forEach(entity -> {
                    projectilesData.add(ProjectileData.saveProjectileData(entity));
                });
            }
            if (!zePizdecForBlockInteraction.contains(player)) {
                zePizdecForBlockInteraction.add(player);
            }

            CapabilityUtil.saveRewindData(player, livingEntitiesData, projectilesData, itemsData, blockData, worldData);
        }
    }

    public static void rewindData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            Queue<LivingEntityData> livingEntityData = CapabilityUtil.getLivingEntityData(player);
            Queue<ProjectileData> projectileData = CapabilityUtil.getProjectileData(player);
            Queue<ItemData> itemData = CapabilityUtil.getItemData(player);
            List<Entity> entities = new ArrayList<>();

            entities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, filter -> livingEntityData != null && !(filter instanceof ArmorStandEntity)));
            entities.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, filter -> itemData != null));
            entities.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, filter -> projectileData != null));
            entities.addAll(MCUtil.entitiesAround(Entity.class, player, range, false, filter -> !(filter instanceof ArmorStandEntity)));
            entities.add(player);
            entities.forEach(entity -> {
                if (entity instanceof LivingEntity)
                    rewindEntityData(livingEntityData, entity, entities);
                if (entity instanceof ProjectileEntity)
                    rewindEntityData(projectileData, entity, entities);
                if (entity instanceof ItemEntity)
                    rewindEntityData(itemData, entity, entities);
            });

            WorldData worldData = CapabilityUtil.getWorldData(player);
            if (worldData != null) {
                WorldData.rewindWorldData(worldData);
            }
            if (zePizdecForBlockInteraction.contains(player)) {
                zePizdecForBlockInteraction.remove(player);
            }
            CapabilityUtil.removeRewindData(player);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T, M> void rewindEntityData(Queue<? extends IEntityData<T, M>> dataQueue, Entity entity, List<Entity> entities) {
        AtomicBoolean foundInWorld = new AtomicBoolean(false);
        dataQueue.forEach(data -> {
            if (!data.isRestored(data) && data.getEntity(data) == entity) {
                foundInWorld.set(true);
                data.rewindData(data);
            }
        });
        if (!foundInWorld.get()) {
            IEntityData<T, M> exampleData = dataQueue.peek();
            if (exampleData != null && !exampleData.inData((Queue<IEntityData<T, M>>) dataQueue, (M) entity)) {
                entity.remove();
            } else {
                dataQueue.forEach(data -> {
                    if (!data.isRestored(data) && entities.contains(entity)) {
                        data.rewindDeadData(data, entity.level);
                    }
                });
            }
        }
    }

    // private static void restoreBlocks(Queue<BlockData> chunk, World world) {
    //     boolean debugged = false;
    //     for (BlockData blockData : chunk) {
    //         BlockPos pos = blockData.pos;
    //         BlockState savedState = blockData.blockState;
    //         BlockState currentState = world.getBlockState(pos);
    //             // System.out.println("Block: " + currentState + " | " + savedState);

    //         if (!currentState.getBlock().equals(savedState.getBlock()) || !currentState.equals(savedState)) {
    //             if (!debugged) {
    //                 System.out.println("Block: " + currentState + " | " + savedState + " | " + pos);
    //             }
    //             debugged = true;
    //             world.setBlockAndUpdate(pos, savedState);
    //         }
    //     }
    // }

    public static boolean getRingoClock(LivingEntity entity, boolean damage, Hand hand) {
        ItemStack itemStack;
        if (hand == Hand.MAIN_HAND) {
            itemStack = entity.getMainHandItem();
        } else {
            itemStack = entity.getOffhandItem();
        }
        if (itemStack.getItem() == InitItems.RINGO_CLOCK.get()) {
            if (!itemStack.hasTag()) {
                CompoundNBT nbt = new CompoundNBT();
                itemStack.setTag(nbt);
                nbt.putInt("cracked", 0);
                return true;
            } else {
                int cracked = itemStack.getTag().getInt("cracked");
                if (cracked < RingoClock.MAX_CAN_BE_CRACKED()) {
                    if (damage) {
                        itemStack.getTag().putInt("cracked", cracked + 1);
                    }
                    return true;
                }
            }
        }    
        return false;
    }

    public static boolean getRingoClock(LivingEntity entity) {
        return getRingoClock(entity, false);
    }

    public static boolean getRingoClock(LivingEntity entity, boolean damage) {
        return getRingoClock(entity, damage, Hand.OFF_HAND);
    }

    public static enum CooldownSystem {
        TIME,
        OWN;
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent event) {
        PlayerEntity player = event.getPlayer();
        AtomicBoolean canceled = new AtomicBoolean(false);
        if (!event.isCancelable() || player == null || !GlobalConfig.getBlockInteractWhileRewind(player.level.isClientSide())) return;

        zePizdecForBlockInteraction.forEach(uniquePlayer -> {
            if (uniquePlayer == player) {
                MandomAddon.LOGGER.warn(1);
                canceled.set(true);
            }
            if (player.distanceTo(uniquePlayer) <= GlobalConfig.getTimeRewindChunks(player.level.isClientSide()) * 16) {
                MandomAddon.LOGGER.warn(2);
                canceled.set(true);
            }
        });
        
        MandomAddon.LOGGER.warn("Blocked INTERACTION: " + canceled.get());
        event.setCanceled(canceled.get());
    }
}
