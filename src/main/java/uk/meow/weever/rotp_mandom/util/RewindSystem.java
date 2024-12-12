package uk.meow.weever.rotp_mandom.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.capability.entity.*;
import uk.meow.weever.rotp_mandom.capability.world.WorldUtilCap;
import uk.meow.weever.rotp_mandom.capability.world.WorldUtilCapProvider;
import uk.meow.weever.rotp_mandom.config.GlobalConfig;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;
import uk.meow.weever.rotp_mandom.event.custom.RemoveEntityEvent;
import uk.meow.weever.rotp_mandom.event.custom.SetBlockEvent;
import uk.meow.weever.rotp_mandom.init.InitItems;
import uk.meow.weever.rotp_mandom.item.RingoClock;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.RWRewindClientPlayerData;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

import static uk.meow.weever.rotp_mandom.util.AddonUtil.entitiesAround;
import static uk.meow.weever.rotp_mandom.util.AddonUtil.getNearbyBlocks;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID)
public class RewindSystem {
    private static final List<BlockData> TEMP_BLOCK_DATA = new ArrayList<>();
    private static final List<Entity> TEMP_ENTITY_DATA = new ArrayList<>();

    public static void rewindData(LivingEntity livingEntity, int range) {
        if (!livingEntity.level.isClientSide()) {
            List<Entity> entities = entitiesAround(Entity.class, livingEntity.level, livingEntity.position(), range, AddonUtil::predicateForRewind);
            List<Entity> deadEntities = getAllDeadEntities(WorldUtilCapProvider.getWorldCap(livingEntity.level).getDeadEntities(), AddonUtil::predicateForRewind);
            entities.addAll(deadEntities);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    LinkedList<LivingEntityData> livingEntityData = CapabilityUtil.getLivingEntityData((LivingEntity) entity);
                    if (livingEntityData != null && !livingEntityData.isEmpty()) {
                        rewindLivingEntityData(livingEntityData.getFirst());
                    }

                    if (entity instanceof PlayerEntity) {
                        AddonPackets.sendToClient(new RWRewindClientPlayerData(entity.getId()), (PlayerEntity) entity);
                    }
                } else if (entity instanceof ProjectileEntity) {
                    LinkedList<ProjectileData> projectileData = CapabilityUtil.getProjectileData((ProjectileEntity) entity);
                    if (projectileData != null && !projectileData.isEmpty()) {
                        rewindProjectileData(projectileData.getFirst());
                    }
                } else if (entity instanceof ItemEntity) {
                    LinkedList<ItemData> itemData = CapabilityUtil.getItemData((ItemEntity) entity);
                    if (itemData != null && !itemData.isEmpty()) {
                        rewindItemData(itemData.getFirst());
                    }
                }
            }

            LinkedList<List<BlockData>> blockData = CapabilityUtil.getBlockData(livingEntity.level);
            if (blockData != null && !blockData.isEmpty()) {
                restoreBlocks(blockData, livingEntity.level);
            }
            LinkedList<WorldData> worldData = CapabilityUtil.getWorldData(livingEntity.level);
            if (worldData != null && !worldData.isEmpty()) {
                WorldData.rewindWorldData(worldData.getFirst());
            }
        }
    }

    private static <T extends Entity> List<Entity> getAllDeadEntities(LinkedList<List<Entity>> linkedListWithDeadEntities, @Nullable Predicate<? super T> filter) {
        List<Entity> list = new ArrayList<>();
        for (List<Entity> linkEntities : linkedListWithDeadEntities) {
            if (filter != null) {
                linkEntities.removeIf(entity -> !filter.test((T) entity));
            }
            list.addAll(linkEntities);
        }
        return list;
    }

    private static void rewindLivingEntityData(LivingEntityData data) {
        if (data.entity.isAlive()) {
            data.rewindLivingEntityData(data);
        } else {
            System.out.println("Dead: " + data.entity.getName().getString());
            data.rewindDeadLivingEntityData(data, data.entity.level);
        }
    }

    private static void rewindProjectileData(ProjectileData data) {
        if (data.entity.isAlive()) {
            data.rewindProjectileData(data);
        } else {
            data.rewindDeadProjectileEntityData(data, data.entity.level);
        }
    }

    private static void rewindItemData(ItemData data) {
        if (data.entity.isAlive()) {
            data.rewindItemData(data);
        } else {
            data.rewindDeadItemData(data, data.entity.level);
        }
    }

    private static void restoreBlocks(LinkedList<List<BlockData>> blockDataList, World world) {
        if (blockDataList == null || world == null) return;
        for (List<BlockData> list : blockDataList) {
            Set<BlockPos> processedBlocks = new HashSet<>();
            List<BlockData> toRestore = new ArrayList<>(list);

            for (BlockData data : toRestore) {
                BlockPos pos = data.pos;
                if (processedBlocks.add(pos)) {
                    BlockData.rewindBlockData(world, data);
                }
            }
        }
    }

    public static boolean getRingoClock(LivingEntity entity, boolean damage, Hand hand) {
        ItemStack itemStack;
        if (hand == Hand.MAIN_HAND) {
            itemStack = entity.getMainHandItem();
        } else {
            itemStack = entity.getOffhandItem();
        }

        if (itemStack.getItem() == InitItems.RINGO_CLOCK.get() && itemStack.getTag() != null) {
            int cracked = itemStack.getTag().getInt("cracked");
            if (cracked < RingoClock.MAX_CAN_BE_CRACKED()) {
                if (damage) {
                    itemStack.getTag().putInt("cracked", cracked + 1);
                }
                if (cracked >= RingoClock.MAX_CAN_BE_CRACKED()) {
                    itemStack.shrink(1);
                }
                return true;
            }
        }    
        return false;
    }

    public static boolean getRingoClock(LivingEntity entity, boolean damage) {
        return getRingoClock(entity, damage, Hand.OFF_HAND);
    }

    public static boolean getRingoClock(LivingEntity entity, HandSide handSide) {
        return getRingoClock(entity, false, handSide == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onSetBlock(SetBlockEvent event) {
        if (!event.getWorld().isClientSide() && !event.getWorld().players().isEmpty()) {
            BlockState oldBlockState = event.getState();
            BlockState newBlockState = event.getNewState();
            BlockPos blockPos = event.getPos();
            BlockData.TransferBlockData transferBlockData = event.getTransferedData();
            BlockData.BlockInfo blockInfo;

            if (!oldBlockState.isAir() && newBlockState.isAir()) blockInfo = BlockData.BlockInfo.BREAKED;
            else if (oldBlockState != newBlockState) blockInfo = BlockData.BlockInfo.PLACED;
            else return;

            BlockData blockData = BlockData.saveBlockData(oldBlockState, blockPos, blockInfo, transferBlockData);
            synchronized (TEMP_BLOCK_DATA) {
                TEMP_BLOCK_DATA.add(blockData);
            }
        }
    }

   @SubscribeEvent(priority = EventPriority.HIGH)
   public static void onEntityRemove(RemoveEntityEvent event) {
        Entity entity = event.getEntity();
        onRemoversEvents(entity);
   } // TODO: Create a custom event via Mixin (this.remove() moment)

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityDie(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        onRemoversEvents(entity);
    }

    private static void onRemoversEvents(Entity entity) {
        if (!entity.level.isClientSide() && !TEMP_ENTITY_DATA.contains(entity)) {
            System.out.println(entity.getName().getString());
            synchronized (TEMP_ENTITY_DATA) {
                TEMP_ENTITY_DATA.add(entity);
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            switch (event.phase) {
                case END:
                    event.world.getCapability(WorldUtilCapProvider.CAPABILITY).ifPresent(WorldUtilCap::tick);
                    ((ServerWorld) event.world).getAllEntities().forEach((entity) -> {
                        if (entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity) && !((LivingEntity) entity).isDeadOrDying()){
                            entity.getCapability(LivingEntityUtilCapProvider.CAPABILITY).ifPresent(LivingEntityUtilCap::tick);
                        } else if (entity instanceof ProjectileEntity && entity.isAlive()) {
                            entity.getCapability(ProjectileEntityUtilCapProvider.CAPABILITY).ifPresent(ProjectileEntityUtilCap::tick);
                        } else if (entity instanceof ItemEntity && entity.isAlive()) {
                            entity.getCapability(ItemEntityUtilCapProvider.CAPABILITY).ifPresent(ItemEntityUtilCap::tick);
                        }
                    });
                case START:
                default:
                    break;
            }
            if (event.world.getGameTime() % 20 == 0) {
                synchronized (TEMP_BLOCK_DATA){
                    onBlockSave(event.world, TEMP_BLOCK_DATA);
                    TEMP_BLOCK_DATA.clear();
                }

                synchronized (TEMP_ENTITY_DATA){
                    int maxSeconds = RewindConfig.getSecond(event.world.isClientSide());
                    event.world.getCapability(WorldUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                        cap.addDeadEntities(TEMP_ENTITY_DATA, maxSeconds);
                    });
                    TEMP_ENTITY_DATA.clear();
                }
            }
        }
    }

    public enum CooldownSystem {
        TIME,
        OWN
    }

    private static void onBlockSave(World level, List<BlockData> blockDatas) {
        if (blockDatas != null && !blockDatas.isEmpty() && !level.isClientSide()) {
            int range = GlobalConfig.getTimeRewindChunks(level.isClientSide()) * 16;
            List<? extends PlayerEntity> players = level.players();
            players.forEach(player -> {
                CapabilityUtil.addBlockData(player.level, getNearbyBlocks(player, blockDatas, range));
            });
        }
    }
}
