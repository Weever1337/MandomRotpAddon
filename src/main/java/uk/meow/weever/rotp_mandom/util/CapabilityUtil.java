package uk.meow.weever.rotp_mandom.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.meow.weever.rotp_mandom.capability.entity.*;
import uk.meow.weever.rotp_mandom.capability.world.WorldUtilCap;
import uk.meow.weever.rotp_mandom.capability.world.WorldUtilCapProvider;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;

import java.util.LinkedList;
import java.util.List;

public class CapabilityUtil {
    public static void addBlockData(World world, List<BlockData> blockData) {
        if (blockData == null || world == null) return;

        world.getCapability(WorldUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            int maxSeconds = RewindConfig.getSecond();
            cap.addBlockData(blockData, maxSeconds);
        });
    }

    public static void addDeadEntity(World world, List<Entity> entityData) {
        if (entityData == null || world == null) return;

        world.getCapability(WorldUtilCapProvider.CAPABILITY).ifPresent(cap -> {
            int maxSeconds = RewindConfig.getSecond();
            cap.addDeadEntities(entityData, maxSeconds);
        });
    }

    public static LinkedList<LivingEntityData> getLivingEntityData(LivingEntity livingEntity) {
        return livingEntity.getCapability(LivingEntityUtilCapProvider.CAPABILITY).map(LivingEntityUtilCap::getLivingEntityData).orElse(null);
    }

    @OnlyIn(Dist.CLIENT)
    public static LinkedList<ClientPlayerData> getClientPlayerData(PlayerEntity player) {
        return player.getCapability(ClientPlayerEntityUtilCapProvider.CAPABILITY).map(ClientPlayerEntityUtilCap::getClientPlayerData).orElse(null);
    }

    public static LinkedList<ProjectileData> getProjectileData(ProjectileEntity projectileEntity) {
        return projectileEntity.getCapability(ProjectileEntityUtilCapProvider.CAPABILITY).map(ProjectileEntityUtilCap::getProjectileData).orElse(null);
    }

    public static LinkedList<ItemData> getItemData(ItemEntity itemEntity) {
        return itemEntity.getCapability(ItemEntityUtilCapProvider.CAPABILITY).map(ItemEntityUtilCap::getItemData).orElse(null);
    }

    public static LinkedList<WorldData> getWorldData(World world) {
        return world.getCapability(WorldUtilCapProvider.CAPABILITY).map(WorldUtilCap::getWorldData).orElse(null);
    }

    public static LinkedList<List<BlockData>> getBlockData(World world) {
        return world.getCapability(WorldUtilCapProvider.CAPABILITY).map(WorldUtilCap::getBlockData).orElse(null);
    }

    public static int getCapabilitySeconds(Entity entity) {
        if (entity instanceof LivingEntity) {
            return getLivingEntityData((LivingEntity) entity).size();
        } else if (entity instanceof ProjectileEntity) {
            return getProjectileData((ProjectileEntity) entity).size();
        } else if (entity instanceof ItemEntity) {
            return getItemData((ItemEntity) entity).size();
        }
        return 0;
    }
}
