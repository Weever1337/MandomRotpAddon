package uk.meow.weever.rotp_mandom.capability.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import uk.meow.weever.rotp_mandom.capability.entity.*;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;

import java.util.LinkedList;
import java.util.List;

public class WorldUtilCap {
    private final World world;
    private final LinkedList<WorldData> worldData = new LinkedList<>();
    private final LinkedList<List<BlockData>> blockData = new LinkedList<>();
    private final LinkedList<List<Entity>> deadEntities = new LinkedList<>();
    private int ticks = 0;

    public WorldUtilCap(World world) {
        this.world = world;
    }

    public void tick() {
        if (world == null || world.isClientSide()) return;
        ticks++;
        int maxSize = RewindConfig.getSecond();

        if (ticks % 20 == 0) {
            ticks = 0;
            addWorldData(WorldData.saveWorldData(world), maxSize);
        }

        ((ServerWorld) world).getAllEntities().forEach((entity) -> {
            if (entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity)) {
                entity.getCapability(LivingEntityUtilCapProvider.CAPABILITY).ifPresent(LivingEntityUtilCap::tick);
            } else if (entity instanceof ProjectileEntity && entity.isAlive()) {
                entity.getCapability(ProjectileEntityUtilCapProvider.CAPABILITY).ifPresent(ProjectileEntityUtilCap::tick);
            } else if (entity instanceof ItemEntity && entity.isAlive()) {
                entity.getCapability(ItemEntityUtilCapProvider.CAPABILITY).ifPresent(ItemEntityUtilCap::tick);
            }
        });
    }

    public LinkedList<List<BlockData>> getBlockData() {
        return blockData;
    }

    public void addBlockData(List<BlockData> blockData, int maxSize) {
        if (this.blockData.size() > maxSize) {
            this.blockData.removeFirst();
        }
        this.blockData.addLast(blockData);
    }

    public void clearBlockData() {
        this.blockData.clear();
    }

    public LinkedList<WorldData> getWorldData() {
        return worldData;
    }

    public void addWorldData(WorldData worldData, int maxSize) {
        if (this.worldData.size() > maxSize) {
            this.worldData.removeFirst();
        }
        this.worldData.addLast(worldData);
    }

    public LinkedList<List<Entity>> getDeadEntities() {
        return deadEntities;
    }

    public void addDeadEntities(List<Entity> tempEntityData, int maxSize) {
        if (this.deadEntities.size() > maxSize) {
            this.deadEntities.removeFirst();
        }
        this.deadEntities.addLast(tempEntityData);
    }
}

