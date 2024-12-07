package uk.meow.weever.rotp_mandom.util;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.config.GlobalConfig;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;
import uk.meow.weever.rotp_mandom.event.custom.SetBlockEvent;
import uk.meow.weever.rotp_mandom.init.InitItems;
import uk.meow.weever.rotp_mandom.init.InitStands;
import uk.meow.weever.rotp_mandom.item.RingoClock;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.RWAddClientPlayerData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static uk.meow.weever.rotp_mandom.util.AddonUtil.*;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID)
public class RewindSystem {
    private static final LinkedList<BlockData> TEMP_BLOCK_DATA = new LinkedList<>();

    public static void saveData(PlayerEntity player){
        List<LivingEntityData> livingEntityData = new ArrayList<>();
        List<ProjectileData> projectileData = new ArrayList<>();
        List<ItemData> itemData = new ArrayList<>();

        WorldData worldData = WorldData.saveWorldData(player.level);
        int range = GlobalConfig.getTimeRewindChunks(player.level.isClientSide()) * 16;

        List<Entity> entities = new ArrayList<>();
        entities.addAll(MCUtil.entitiesAround(Entity.class, player, range, false, filter ->
                (filter instanceof LivingEntity || filter instanceof ProjectileEntity || filter instanceof ItemEntity) && !(filter instanceof ArmorStandEntity)));
        entities.add(player);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
//                if (entity instanceof PlayerEntity) {
//                    AddonPackets.sendToClient(new RWAddClientPlayerData(entity.getId(), player.getId()), player); // TODO: do this more... better?
//                }
                livingEntityData.add(LivingEntityData.saveLivingEntityData((LivingEntity) entity));
            } else if (entity instanceof ItemEntity) {
                itemData.add(ItemData.saveItemData((ItemEntity) entity));
            } else if (entity instanceof ProjectileEntity) {
                projectileData.add(ProjectileData.saveProjectileData((ProjectileEntity) entity));
            }
        }

        CapabilityUtil.addLivingEntityData(player, livingEntityData);
        CapabilityUtil.addProjectileEntityData(player, projectileData);
        CapabilityUtil.addItemEntityData(player, itemData);
        CapabilityUtil.addWorldData(player, worldData);
    }

    public static void rewindData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            LinkedList<List<BlockData>> blockData = CapabilityUtil.getBlockData(player);
            if (blockData != null && !blockData.isEmpty()) {
                restoreBlocks(blockData, player.level);
            }

            LinkedList<List<ClientPlayerData>> clientPlayerData = CapabilityUtil.getClientPlayerData(player);
            if (clientPlayerData != null && !clientPlayerData.isEmpty()) {
                rewindClientPlayerData(clientPlayerData.getFirst());
            }

            LinkedList<List<LivingEntityData>> livingEntityData = CapabilityUtil.getLivingEntityData(player);
            if (livingEntityData != null && !livingEntityData.isEmpty()) {
                rewindLivingEntityData(livingEntityData.getFirst());
            }

            LinkedList<List<ProjectileData>> projectileData = CapabilityUtil.getProjectileData(player);
            if (projectileData != null && !projectileData.isEmpty()) {
                rewindProjectileData(projectileData.getFirst());
            }

            LinkedList<List<ItemData>> itemData = CapabilityUtil.getItemData(player);
            if (itemData != null && !itemData.isEmpty()) {
                rewindItemData(itemData.getFirst());
            }

            LinkedList<WorldData> worldData = CapabilityUtil.getWorldData(player);
            if (worldData != null && !worldData.isEmpty()) {
                WorldData.rewindWorldData(worldData.getFirst());
            }

            CapabilityUtil.removeRewindData(player);
        }
    }

    private static void rewindLivingEntityData(List<LivingEntityData> livingEntityData) {
        for (LivingEntityData data : livingEntityData) {
            if (data.entity.isAlive()) {
                System.out.println("[L] DEFAULT: " + data.entity.getName().getString());
                data.rewindLivingEntityData(data);
            } else {
                System.out.println("[L] DIED: " + data.entity.getName().getString());
                data.rewindDeadLivingEntityData(data, data.entity.level);
            }
        }
    }

    private static void rewindProjectileData(List<ProjectileData> projectileData) {
        for (ProjectileData data : projectileData) {
            if (data.entity.isAlive()) {
                System.out.println("[P] DEFAULT: " + data.entity.getName().getString());
                data.rewindProjectileData(data);
            } else {
                System.out.println("[P] DIED: " + data.entity.getName().getString());
                data.rewindDeadProjectileEntityData(data, data.entity.level);
            }
        }
    }

    private static void rewindItemData(List<ItemData> itemData) {
        for (ItemData data : itemData) {
            if (data.entity.isAlive()) {
                System.out.println("[I] DEFAULT: " + data.entity.getName().getString());
                data.rewindItemData(data);
            } else {
                System.out.println("[I] DIED: " + data.entity.getName().getString());
                data.rewindDeadItemData(data, data.entity.level);
            }
        }
    }

    private static void restoreBlocks(LinkedList<List<BlockData>> blockDataList, World world) {
        if (blockDataList == null || world == null) return;
        for (List<BlockData> list : blockDataList) {
            List<BlockPos> processedBlocks = new ArrayList<>();
            List<BlockData> toRestore = new ArrayList<>(list);

            for (BlockData data : toRestore) {
                BlockPos pos = data.pos;
                if (processedBlocks.add(pos)) {
                    BlockData.rewindBlockData(world, data, processedBlocks);
                }
            }
        }
    }

    private static void rewindClientPlayerData(List<ClientPlayerData> dataList) {
        for (ClientPlayerData clientData : dataList) {
            PlayerEntity entity = clientData.player;
            if (!entity.isAlive()) return;
            clientData.rewindClientPlayerData(clientData);
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

    public static boolean getRingoClock(LivingEntity entity, boolean damage) {
        return getRingoClock(entity, damage, Hand.OFF_HAND);
    }

//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void onSetBlock(SetBlockEvent event) {
//        if (!event.getWorld().isClientSide() && !event.getWorld().players().isEmpty()) {
//            BlockState oldBlockState = event.getState();
//            BlockState newBlockState = event.getNewState();
//            BlockPos blockPos = event.getPos();
//            BlockData.TransferBlockData transferBlockData = event.getTransferedData();
//            BlockData.BlockInfo blockInfo;
//
//            if (!oldBlockState.isAir() && newBlockState.isAir()) blockInfo = BlockData.BlockInfo.BREAKED;
//            else if (oldBlockState != newBlockState) blockInfo = BlockData.BlockInfo.PLACED;
//            else return;
//
//            BlockData blockData = BlockData.saveBlockData(oldBlockState, blockPos, blockInfo, transferBlockData);
//            synchronized(TEMP_BLOCK_DATA){
//                TEMP_BLOCK_DATA.add(blockData);
//            }
//        }
//    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!event.world.isClientSide()){
            if (event.world.getGameTime() % 20 == 0) {
                synchronized (TEMP_BLOCK_DATA){
                    onBlockSave(event.world, TEMP_BLOCK_DATA);
                    TEMP_BLOCK_DATA.clear();
                }
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        if (player == null || !IStandPower.getStandPowerOptional(player).isPresent() || player.level.isClientSide() || player.tickCount % 20 != 0 || TimeStopHandler.isTimeStopped(player.level, player.blockPosition()))
            return;

        IStandPower power = IStandPower.getStandPowerOptional(player).orElse(null);
        if (power.getType() == InitStands.MANDOM.getStandType()) {
            saveData(player);
        }
    }

    public enum CooldownSystem {
        TIME,
        OWN
    }

    private static void onBlockSave(World level, LinkedList<BlockData> blockDatas) {
        if (blockDatas != null && !blockDatas.isEmpty()) {
            int range = GlobalConfig.getTimeRewindChunks(level.isClientSide()) * 16;
            List<? extends PlayerEntity> players = level.players();

            players.forEach(player -> {
//                System.out.println(player.getName().getString());
//                CapabilityUtil.addBlockData(player, getNearbyBlocks(player, blockDatas, range));
            });
        }
    }
}
