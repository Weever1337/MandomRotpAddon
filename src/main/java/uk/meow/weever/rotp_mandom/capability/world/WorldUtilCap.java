package uk.meow.weever.rotp_mandom.capability.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;

import javax.swing.text.html.parser.Entity;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class WorldUtilCap {
    private final ServerWorld world;
    private UUID serverId;
    private final LinkedList<WorldData> worldData = new LinkedList<>();
    private final LinkedList<List<BlockData>> blockData = new LinkedList<>();
    private LinkedList<List<Entity>> diedEntities = new LinkedList<>();

    public WorldUtilCap(ServerWorld world) {
        this.world = world;
        if (!world.isClientSide()) {
            this.serverId = UUID.randomUUID();
        }
    }

    public void tick() {
        int maxSize = RewindConfig.getSecond(world.isClientSide());

        if (world.getGameTime() % 20 == 0) {
            if (this.blockData.size() > maxSize) {
                this.blockData.removeFirst();
            }

            if (this.worldData.size() > maxSize) {
                this.worldData.removeFirst();
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

    public LinkedList<List<Entity>> getDiedEntities() {
        return diedEntities;
    }

    public void addDiedEntities(List<Entity> diedEntities) {
        this.diedEntities.addLast(diedEntities);
    }

    CompoundNBT save() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("ServerId", serverId);
        return nbt;
    }

    void load(CompoundNBT nbt) {
        if (nbt.hasUUID("ServerId")) {
            serverId = nbt.getUUID("ServerId");
        }
    }
}
