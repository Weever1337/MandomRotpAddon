package uk.meow.weever.rotp_mandom.util;

import com.github.standobyte.jojo.util.mc.MCUtil;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.meow.weever.rotp_mandom.data.entity.*;
import uk.meow.weever.rotp_mandom.data.world.*;
import uk.meow.weever.rotp_mandom.data.world.BlockData.BlockInfo;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.config.*;
import uk.meow.weever.rotp_mandom.init.InitItems;
import uk.meow.weever.rotp_mandom.item.RingoClock;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.RWAddClientPlayerData;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID)
public class RewindSystem {
    public static void saveData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            CapabilityUtil.removeRewindData(player);

            Queue<LivingEntityData> livingEntitiesData = new LinkedList<>();
            Queue<ProjectileData> projectilesData = new LinkedList<>();
            Queue<ItemData> itemsData = new LinkedList<>();
            WorldData worldData = null;

            if (TPARConfig.getSaveWorld(player.level.isClientSide())) {
                worldData = WorldData.saveWorldData(player.level);
            }
            if (TPARConfig.getSaveEntities(player.level.isClientSide())) {
                List<LivingEntity> livingEntities = new ArrayList<>();
                livingEntities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, entity -> (!(entity instanceof ArmorStandEntity))));
                livingEntities.add(player);
                livingEntities.forEach(entity -> {
                    if (entity instanceof PlayerEntity) {
                        AddonPackets.sendToClient(new RWAddClientPlayerData(entity.getId(), player.getId()), player);
                    }
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

            CapabilityUtil.saveRewindData(player, livingEntitiesData, projectilesData, itemsData, worldData);
        }
    }

    public static void rewindData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            Queue<LivingEntityData> livingEntityData = CapabilityUtil.getLivingEntityData(player);
            Set<ClientPlayerData> clientPlayerData = CapabilityUtil.getClientPlayerData(player);
            Queue<ProjectileData> projectileData = CapabilityUtil.getProjectileData(player);
            Queue<ItemData> itemData = CapabilityUtil.getItemData(player);
            Queue<BlockData> blockData = CapabilityUtil.getBlockData(player);
            List<Entity> entities = new ArrayList<>();
            if (blockData != null) {
                restoreBlocks(blockData, player.level);
            }

            entities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, filter -> livingEntityData != null && !(filter instanceof ArmorStandEntity)));
            entities.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, filter -> itemData != null));
            entities.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, filter -> projectileData != null));
            entities.addAll(MCUtil.entitiesAround(Entity.class, player, range, false, filter -> !(filter instanceof ArmorStandEntity)));
            entities.add(player);
            entities.forEach(entity -> {
                if (entity instanceof PlayerEntity) {   
                    clientPlayerData.forEach(data -> {
                        ClientPlayerData.rewindClientPlayerData(data);
                    });
                }
                if (entity instanceof LivingEntity) {
                    rewindEntityData(livingEntityData, entity, entities);
                }
                if (entity instanceof ProjectileEntity)
                    rewindEntityData(projectileData, entity, entities);
                if (entity instanceof ItemEntity)
                    rewindEntityData(itemData, entity, entities);
            });

            WorldData worldData = CapabilityUtil.getWorldData(player);
            if (worldData != null) {
                WorldData.rewindWorldData(worldData);
            }
            CapabilityUtil.removeRewindData(player);
        }
    }

    private static void restoreBlocks(Queue<BlockData> blockDataQueue, World world) {
        Set<BlockPos> processedBlocks = new HashSet<>();
    
        for (BlockData data : blockDataQueue) {
            BlockPos pos = data.pos;
            BlockState currentState = world.getBlockState(pos);
            BlockInfo blockInfo = data.blockInfo;
            if (processedBlocks.contains(pos) && !BlockData.inData(blockDataQueue, currentState, pos, blockInfo)) {
                continue;
            }
            BlockData.rewindBlockData(world, data, processedBlocks);
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

    // !!!!!!!!!!! BLOCK RESTORE, W.I.P FEATURE !!!!!!!!!!!
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerInteractRightClick(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        BlockPos blockPos = event.getPos();
        if (player == null || blockPos == null) return;
        BlockState blockState = player.level.getBlockState(blockPos);
        onBlockSave(player, blockState, blockPos, BlockInfo.INTERACTED);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBlockDestroy(BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getState();
        if (player == null) return;
        onBlockSave(player, blockState, blockPos, BlockInfo.BREAKED);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onExplosion(ExplosionEvent.Detonate event) {
        for (BlockPos blockPos : event.getAffectedBlocks()) {
            BlockState blockState = event.getWorld().getBlockState(blockPos);
            if (blockState.getBlock() != Blocks.AIR) {
                onBlockSave(event.getExplosion().getExploder(), blockState, blockPos, BlockInfo.BREAKED);
            }
        }
    }
    
    private static void onBlockSave(Entity entity, BlockState blockState, BlockPos blockPos, BlockInfo blockInfo) {
        BlockData blockData = BlockData.saveBlockData(entity.level, blockState, blockPos, blockInfo);
        int range = GlobalConfig.getTimeRewindChunks(entity.level.isClientSide()) * 16;
        List<PlayerEntity> playersAround = new ArrayList<>(MCUtil.entitiesAround(PlayerEntity.class, entity, range, false, null));

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!CapabilityUtil.dataIsEmptyOrNot(player) && !BlockData.inData(CapabilityUtil.getBlockData(player), blockState, blockPos, blockInfo)) {
                CapabilityUtil.addBlockData(player, blockData);
            }
        }

        playersAround.forEach(playerAround -> {
            if (playerAround != entity && playerAround.distanceTo(entity) <= range && !CapabilityUtil.dataIsEmptyOrNot(playerAround) && !BlockData.inData(CapabilityUtil.getBlockData(playerAround), blockState, blockPos, blockInfo)) {
                CapabilityUtil.addBlockData(playerAround, blockData);
            }
        });
    }
}
