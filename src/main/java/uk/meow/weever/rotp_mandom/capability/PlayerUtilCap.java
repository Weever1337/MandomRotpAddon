package uk.meow.weever.rotp_mandom.capability;

import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
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
import java.util.List;
import java.util.List;

public class PlayerUtilCap {
    private final PlayerEntity player;
    private List<LivingEntityData> livingEntityData = new LinkedList<>();
    private List<ProjectileData> projectileData = new LinkedList<>();
    private List<ItemData> itemData = new LinkedList<>();
    private List<BlockData> blockData = new LinkedList<>();
    private List<ClientPlayerData> clientPlayerEntityData = new LinkedList<>();
    private WorldData worldData = null;

    private boolean dataIsEmpty = true;

    public PlayerUtilCap(PlayerEntity player) {
        this.player = player;
    }

    public List<LivingEntityData> getLivingEntityData() {
        return livingEntityData;
    }

    public void setLivingEntityData(List<LivingEntityData> livingEntityData) {
        this.livingEntityData = livingEntityData;
    }

    public List<ClientPlayerData> getClientPlayerData() {
        return clientPlayerEntityData;
    }

    public void setClientPlayerData(List<ClientPlayerData> cData) {
        this.clientPlayerEntityData = cData;
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

    public List<ProjectileData> getProjectileData() {
        return projectileData;
    }

    public void setProjectileData(List<ProjectileData> projectileData) {
        this.projectileData = projectileData;
    }

    public List<ItemData> getItemData() {
        return itemData;
    }

    public void setItemData(List<ItemData> itemData) {
        this.itemData = itemData;
    }

    public List<BlockData> getBlockData() {
        return blockData;
    }

    public void setBlockData(List<BlockData> data) {
        this.blockData = data;
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
