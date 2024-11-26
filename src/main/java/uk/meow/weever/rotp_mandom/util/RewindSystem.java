package uk.meow.weever.rotp_mandom.util;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.config.GlobalConfig;
import uk.meow.weever.rotp_mandom.data.entity.*;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.BlockData.BlockInfo;
import uk.meow.weever.rotp_mandom.data.world.WorldData;
import uk.meow.weever.rotp_mandom.event.custom.SetBlockEvent;
import uk.meow.weever.rotp_mandom.init.InitItems;
import uk.meow.weever.rotp_mandom.item.RingoClock;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.RWAddClientPlayerData;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static uk.meow.weever.rotp_mandom.util.AddonUtil.convertFromBlockPos;
import static uk.meow.weever.rotp_mandom.util.AddonUtil.entitiesAround;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID)
public class RewindSystem {
    public static void saveData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            CapabilityUtil.removeRewindData(player);

            List<LivingEntityData> livingEntitiesData = new LinkedList<>();
            List<ProjectileData> projectilesData = new LinkedList<>();
            List<ItemData> itemsData = new LinkedList<>();
            WorldData worldData = WorldData.saveWorldData(player.level);

            List<Entity> entities = new ArrayList<>();

            entities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, filter -> !(filter instanceof ArmorStandEntity)));
            entities.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, null));
            entities.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, null));
//            entities.addAll(MCUtil.entitiesAround(Entity.class, player, range, false, filter -> filter instanceof ItemEntity));
            entities.add(player);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    if (entity instanceof PlayerEntity) {
                        AddonPackets.sendToClient(new RWAddClientPlayerData(entity.getId(), player.getId()), player); // TODO: do this more... better?
                    }
                    livingEntitiesData.add(LivingEntityData.saveLivingEntityData((LivingEntity) entity));
                } else if (entity instanceof ItemEntity) {
                    System.out.println(entity.getName().getString());
                    itemsData.add(ItemData.saveItemData((ItemEntity) entity));
                } else if (entity instanceof ProjectileEntity) {
                    projectilesData.add(ProjectileData.saveProjectileData((ProjectileEntity) entity));
                }
            }
            CapabilityUtil.saveRewindData(player, livingEntitiesData, projectilesData, itemsData, worldData);
        }
    }

    public static void rewindData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            List<LivingEntityData> livingEntityData = CapabilityUtil.getLivingEntityData(player);
            List<ClientPlayerData> clientPlayerData = CapabilityUtil.getClientPlayerData(player);
            List<ProjectileData> projectileData = CapabilityUtil.getProjectileData(player);
            List<ItemData> itemData = CapabilityUtil.getItemData(player);
            List<BlockData> blockData = CapabilityUtil.getBlockData(player);
            List<Entity> entities = new ArrayList<>();
            entities.addAll(MCUtil.entitiesAround(Entity.class, player, range, false, filter -> !(filter instanceof ArmorStandEntity)));
            restoreBlocks(blockData, player.level);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity && livingEntityData != null) {
                    if (entity instanceof PlayerEntity && clientPlayerData != null) {
                        rewindClientPlayerData(clientPlayerData);
                    }
                    rewindLivingEntityData(livingEntityData, entity, entities);
                } else if (entity instanceof ProjectileEntity && projectileData != null) {
                    rewindProjectileData(projectileData, entity, entities);
                } else if (entity instanceof ItemEntity && itemData != null) {
                    rewindItemData(itemData, entity, entities);
                }
            }

            WorldData.rewindWorldData(CapabilityUtil.getWorldData(player));
            CapabilityUtil.removeRewindData(player);
        }
    }

    private static void restoreBlocks(List<BlockData> blockDataList, World world) {
        if (blockDataList == null || world == null) return;
        List<BlockPos> processedBlocks = new HashList<>();
        List<BlockData> toRestore = new ArrayList<>(blockDataList);

        for (BlockData data : toRestore) {
            BlockPos pos = data.pos;
            if (processedBlocks.contains(pos)) {
                continue;
            }
            BlockData.rewindBlockData(world, data, processedBlocks);
        }
    }

    private static void rewindClientPlayerData(List<ClientPlayerData> dataList) {
        for (ClientPlayerData clientData : dataList) {
            PlayerEntity entity = clientData.player;
            if (!entity.isAlive()) return;
            clientData.rewindClientPlayerData(clientData);
        }
    }

    private static void rewindLivingEntityData(List<LivingEntityData> dataList, Entity entity, List<Entity> entities) {
        AtomicBoolean foundInWorld = new AtomicBoolean(false);
        dataList.forEach(data -> {
            if (!data.restored && data.entity == entity) {
                foundInWorld.set(true);
                if (entities.contains(data.entity)) {
                    data.rewindLivingEntityData(data);
                }
            }
        });
        if (!foundInWorld.get()) {
            if (inLivingEntityData(dataList, (LivingEntity) entity)) {
                dataList.forEach(data -> {
                    if (!data.restored && !entities.contains(data.entity)) {
                        data.rewindDeadLivingEntityData(data, entity.level);
                    }
                });
            }
        }
    }

    private static void rewindProjectileData(List<ProjectileData> projectileData, Entity entity, List<Entity> entities) {
        AtomicBoolean foundInWorld = new AtomicBoolean(false);
        projectileData.forEach(data -> {
            if (!data.restored && data.entity == entity) {
                foundInWorld.set(true);
                if (entities.contains(data.entity)) {
                    data.rewindProjectileData(data);
                }
            }
        });
        if (!foundInWorld.get()) {
            if (!inProjectileData(projectileData, (ProjectileEntity) entity)) {
                entity.remove();
            } else {
                projectileData.forEach(data -> {
                    if (!data.restored && !entities.contains(entity)) {
                        data.rewindDeadProjectileEntityData(data, entity.level);
                    }
                });
            }
        }
    }

    private static void rewindItemData(List<ItemData> itemData, Entity entity, List<Entity> entities) {
        AtomicBoolean foundInWorld = new AtomicBoolean(false);
        itemData.forEach(data -> {
            if (!data.restored && data.entity == entity) {
                foundInWorld.set(true);
                if (inItemData(itemData, (ItemEntity) entity)) {
                    data.rewindItemData(data);
                }
            }
        });
        if (!foundInWorld.get()) {
            if (!inItemData(itemData, (ItemEntity) entity)) {
                entity.remove();
            } else {
                itemData.forEach(data -> {
                    if (!data.restored && !entities.contains(entity)) {
                        data.rewindDeadItemData(data, entity.level);
                    }
                });
            }
        }
    }

    public static boolean inLivingEntityData(List<? extends LivingEntityData> livingEntityData, LivingEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        livingEntityData.forEach(data -> {
            if (data.entity == entity) {
                inData.set(true);
            }
        });
        return inData.get();
    }

    public static boolean inItemData(List<ItemData> itemData, ItemEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        itemData.forEach(data -> {
            if (data.entity == entity) {
                inData.set(true);
            }
        });
        return inData.get();
    }

    public static boolean inProjectileData(List<ProjectileData> projectileData, ProjectileEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        projectileData.forEach(data -> {
            if (data.entity == entity) {
                inData.set(true);
            }
        });
        return inData.get();
    }

    public static boolean getRingoClock(LivingEntity entity, boolean damage, Hand hand) {
        ItemStack itemStack;
        if (hand == Hand.MAIN_HAND) {
            itemStack = entity.getMainHandItem();
        } else {
            itemStack = entity.getOffhandItem();
        }

        if (itemStack.getItem() == InitItems.RINGO_CLOCK.get()) {
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onSetBlock(SetBlockEvent event) {
        if (!event.getWorld().isClientSide() && !event.getWorld().players().isEmpty()) {
            BlockState oldBlockState = event.getState();
            BlockState newBlockState = event.getNewState();
            BlockPos blockPos = event.getPos();
            BlockData.TransferBlockData transferBlockData = event.getTransferedData();
            BlockInfo blockInfo;

            if (!oldBlockState.isAir() && newBlockState.isAir()) blockInfo = BlockInfo.BREAKED;
            else if (oldBlockState != newBlockState) blockInfo = BlockInfo.PLACED;
            else return;

//            System.out.println(oldBlockState.getBlock().getName().getString() + " | " + newBlockState.getBlock().getName().getString() + " | " + blockInfo.name());
            onBlockSave((World) event.getWorld(), oldBlockState, blockPos, blockInfo, transferBlockData);
        }
    }

    public enum CooldownSystem {
        TIME,
        OWN
    }

    private static void onBlockSave(World level, BlockState blockState, BlockPos blockPos, BlockInfo blockInfo, BlockData.TransferBlockData transferBlockData) {
        BlockData blockData = BlockData.saveBlockData(blockState, blockPos, blockInfo, transferBlockData);
        int range = GlobalConfig.getTimeRewindChunks(level.isClientSide()) * 16;
        Vector3d position = convertFromBlockPos(blockPos);
        List<PlayerEntity> playersAround = new ArrayList<>(entitiesAround(PlayerEntity.class, level, position, range, null));

        playersAround.forEach(playerAround -> {
            if (playerAround.distanceToSqr(position) <= range && !CapabilityUtil.dataIsEmptyOrNot(playerAround) && !BlockData.inData(CapabilityUtil.getBlockData(playerAround), blockPos)) {
                CapabilityUtil.addBlockData(playerAround, blockData);
            }
        });
    }
}
