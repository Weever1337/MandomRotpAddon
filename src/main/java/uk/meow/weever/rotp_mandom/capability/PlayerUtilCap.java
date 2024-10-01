package uk.meow.weever.rotp_mandom.capability;

import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.TrSetDataIsEmptyPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.LinkedList;
import java.util.Queue;

public class PlayerUtilCap {
    private final PlayerEntity player;

    private Queue<LivingEntityData> livingEntityData = new LinkedList<>();
    private Queue<ProjectileData> projectileData = new LinkedList<>();
    private Queue<ItemData> itemData = new LinkedList<>();
    private Queue<BlockData> inventoryBlockData = new LinkedList<>();
    private WorldData worldData = null;

    private boolean dataIsEmpty = true;

    public PlayerUtilCap(PlayerEntity player) {
        this.player = player;
    }

    public Queue<LivingEntityData> getLivingEntityData() {
        return livingEntityData;
    }

    public void setLivingEntityData(Queue<LivingEntityData> livingEntityData) {
        this.livingEntityData = livingEntityData;
    }

    public boolean getDataIsEmpty() {
        return dataIsEmpty;
    }

    public void setDataIsEmpty(boolean dataIsEmpty) {
        this.dataIsEmpty = dataIsEmpty;
        if (!player.level.isClientSide()) {
            AddonPackets.sendToClientsTrackingAndSelf(new TrSetDataIsEmptyPacket(player.getId(), dataIsEmpty), player);
        }
    }

    public Queue<ProjectileData> getProjectileData() {
        return projectileData;
    }

    public void setProjectileData(Queue<ProjectileData> projectileData) {
        this.projectileData = projectileData;
    }

    public Queue<ItemData> getItemData() {
        return itemData;
    }

    public void setItemData(Queue<ItemData> itemData) {
        this.itemData = itemData;
    }

    public Queue<BlockData> getInventoryBlockData() {
        return inventoryBlockData;
    }

    public void setInventoryBlockData(Queue<BlockData> data) {
        this.inventoryBlockData = data;
    }

    public WorldData getWorldData() {
        return worldData;
    }

    public void setWorldData(WorldData worldData) {
        this.worldData = worldData;
    }

    public void syncWithAnyPlayer(ServerPlayerEntity player) {
        AddonPackets.sendToClient(new TrSetDataIsEmptyPacket(player.getId(), dataIsEmpty), player);
    }

    public CompoundNBT toNBT() {
        return new CompoundNBT();
    }

    public void fromNBT(CompoundNBT ignored) {
    }
}
