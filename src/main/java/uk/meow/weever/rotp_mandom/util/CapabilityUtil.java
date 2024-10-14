package uk.meow.weever.rotp_mandom.util;

import uk.meow.weever.rotp_mandom.capability.PlayerUtilCap;
import uk.meow.weever.rotp_mandom.capability.PlayerUtilCapProvider;
import net.minecraft.entity.player.PlayerEntity;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;

import java.util.Queue;

public class CapabilityUtil {
    public static boolean dataIsEmptyOrNot(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getDataIsEmpty).orElse(true);
    }

    public static void saveRewindData(
            PlayerEntity player,
            Queue<LivingEntityData> livingEntityData,
            Queue<ProjectileData> projectileData, Queue<ItemData> itemData,
            Queue<BlockData> blockData,
            WorldData worldData
    ) {
        // blockData.forEach(data -> {
        //     System.out.println("Block: " + data.blockState);
        // });
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            cap.setLivingEntityData(livingEntityData);
            cap.setProjectileData(projectileData);
            cap.setItemData(itemData);
            cap.setWorldData(worldData);
            cap.setBlockData(blockData);
            cap.setDataIsEmpty(false);
        });
    }

    public static void removeRewindData(PlayerEntity player) {
        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            cap.setLivingEntityData(null);
            cap.setProjectileData(null);
            cap.setItemData(null);
            cap.setWorldData(null);
            cap.setBlockData(null);
            cap.setDataIsEmpty(true);
        });
    }

    public static Queue<LivingEntityData> getLivingEntityData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getLivingEntityData).orElse(null);
    }

    public static Queue<ProjectileData> getProjectileData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getProjectileData).orElse(null);
    }

    public static Queue<ItemData> getItemData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getItemData).orElse(null);
    }

    public static WorldData getWorldData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getWorldData).orElse(null);
    }

    public static Queue<BlockData> getBlockData(PlayerEntity player) {
        return player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(PlayerUtilCap::getBlockData).orElse(null);
    }
}
