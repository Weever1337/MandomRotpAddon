package uk.meow.weever.rotp_mandom.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;

import java.util.LinkedList;
import java.util.List;

public class PlayerUtilCap {
    private final PlayerEntity player; // wtf is here
    private final LinkedList<List<LivingEntityData>> livingEntityData = new LinkedList<>();
    private final LinkedList<List<ProjectileData>> projectileData = new LinkedList<>();
    private final LinkedList<List<ItemData>> itemData = new LinkedList<>();
    private final LinkedList<List<BlockData>> blockData = new LinkedList<>();
    private final LinkedList<List<ClientPlayerData>> clientPlayerEntityData = new LinkedList<>();
    private final LinkedList<WorldData> worldData = new LinkedList<>();

    public PlayerUtilCap(PlayerEntity player) {
        this.player = player;
    }

    public LinkedList<List<LivingEntityData>> getLivingEntityData() {
        return livingEntityData;
    }

    public void addLivingEntityData(List<LivingEntityData> livingEntityData, int maxSize) {
        if (this.livingEntityData.size() > maxSize) {
            this.livingEntityData.removeFirst();
        }
        this.livingEntityData.addLast(livingEntityData);
    }

    public LinkedList<List<ClientPlayerData>> getClientPlayerData() {
        return clientPlayerEntityData;
    }

    public void addClientPlayerData(List<ClientPlayerData> cData, int maxSize) {
        if (this.clientPlayerEntityData.size() > maxSize) {
            this.clientPlayerEntityData.removeFirst();
        }
        this.clientPlayerEntityData.addLast(cData);
    }

    public LinkedList<List<ProjectileData>> getProjectileData() {
        return projectileData;
    }

    public void addProjectileData(List<ProjectileData> projectileData, int maxSize) {
        if (this.projectileData.size() > maxSize) {
            this.projectileData.removeFirst();
        }
        this.projectileData.addLast(projectileData);
    }

    public LinkedList<List<ItemData>> getItemData() {
        return itemData;
    }

    public void addItemData(List<ItemData> itemData, int maxSize) {
        if (this.itemData.size() > maxSize) {
            this.itemData.removeFirst();
        }
        this.itemData.addLast(itemData);
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

    public void clear() {
        livingEntityData.clear();
        projectileData.clear();
        clientPlayerEntityData.clear();
        itemData.clear();
        blockData.clear();
        worldData.clear();
    }

    public void syncWithAnyPlayer(ServerPlayerEntity player) {
//        AddonPackets.sendToClient(new TrSetDataIsEmptyPacket(player.getId(), dataIsEmpty), player);
    }

    public CompoundNBT toNBT() {
        return new CompoundNBT();
    }

    public void fromNBT(CompoundNBT ignored) {
    }
}
