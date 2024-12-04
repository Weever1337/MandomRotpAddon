package uk.meow.weever.rotp_mandom.util;

import net.minecraft.entity.player.PlayerEntity;
import uk.meow.weever.rotp_mandom.capability.PlayerUtilCap;
import uk.meow.weever.rotp_mandom.capability.PlayerUtilCapProvider;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CapabilityUtil {
    @Deprecated
    public static boolean dataIsEmptyOrNot(PlayerEntity player) {
        return false;
    }

    public static void removeRewindData(PlayerEntity player) {
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(PlayerUtilCap::clear);
    }

    public static void addLivingEntityData(PlayerEntity player, List<LivingEntityData> livingEntityData) {
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            int maxSeconds = RewindConfig.getSecond(player.level.isClientSide());
            cap.addLivingEntityData(livingEntityData, maxSeconds);
        });
    }

    public static void addProjectileEntityData(PlayerEntity player, List<ProjectileData> projectileData) {
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            int maxSeconds = RewindConfig.getSecond(player.level.isClientSide());
            cap.addProjectileData(projectileData, maxSeconds);
        });
    }

    public static void addItemEntityData(PlayerEntity player, List<ItemData> itemData) {
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            int maxSeconds = RewindConfig.getSecond(player.level.isClientSide());
            cap.addItemData(itemData, maxSeconds);
        });
    }

    public static void addClientPlayerData(PlayerEntity player, ClientPlayerData clientPlayerData) {
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            int maxSeconds = RewindConfig.getSecond(player.level.isClientSide());
            List<ClientPlayerData> list = new ArrayList<>();
            list.add(clientPlayerData);
            cap.addClientPlayerData(list, maxSeconds);
        });
    }

    public static void addBlockData(PlayerEntity player, List<BlockData> blockData) {
        if (blockData == null || blockData.isEmpty()) return;

        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            int maxSeconds = RewindConfig.getSecond(player.level.isClientSide());
            cap.addBlockData(blockData, maxSeconds);
        });
    }

    public static void addWorldData(PlayerEntity player, WorldData worldData) {
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            int maxSeconds = RewindConfig.getSecond(player.level.isClientSide());
            cap.addWorldData(worldData, maxSeconds);
        });
    }

    public static LinkedList<List<LivingEntityData>> getLivingEntityData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getLivingEntityData).orElse(null);
    }

    public static LinkedList<List<ClientPlayerData>> getClientPlayerData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getClientPlayerData).orElse(null);
    }

    public static LinkedList<List<ProjectileData>> getProjectileData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getProjectileData).orElse(null);
    }

    public static LinkedList<List<ItemData>> getItemData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getItemData).orElse(null);
    }

    public static LinkedList<WorldData> getWorldData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getWorldData).orElse(null);
    }

    public static LinkedList<List<BlockData>> getBlockData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getBlockData).orElse(null);
    }
}
