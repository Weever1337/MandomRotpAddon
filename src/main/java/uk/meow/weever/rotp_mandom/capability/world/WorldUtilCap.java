package uk.meow.weever.rotp_mandom.capability.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;
import uk.meow.weever.rotp_mandom.util.AddonUtil;

import java.util.LinkedList;
import java.util.List;

public class WorldUtilCap {
    private final World world;
    private final LinkedList<WorldData> worldData = new LinkedList<>();
    private final LinkedList<List<BlockData>> blockData = new LinkedList<>();
    private final LinkedList<List<Entity>> deadEntities = new LinkedList<>();

    public WorldUtilCap(World world) {
        this.world = world;
    }

    public void tick() {
        if (world == null || world.isClientSide()) return;
        int maxSize = RewindConfig.getSecond(world.isClientSide());
//        System.out.println(world.dimension().location());

        if (AddonUtil.oneSecond()) {
            if (this.blockData.size() > maxSize) {
                this.blockData.removeFirst();
            }

            if (this.worldData.size() > maxSize) {
                this.worldData.removeFirst();
            }

            if (this.deadEntities.size() > maxSize) {
                this.deadEntities.removeFirst();
            }
            addWorldData(WorldData.saveWorldData(world), maxSize);
        }
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

