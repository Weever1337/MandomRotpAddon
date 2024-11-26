package uk.meow.weever.rotp_mandom.data.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.data.global.InventorySaver;

public class ClientPlayerData {
    public PlayerEntity player;
    public UUID playerUuid;
    private final ItemStack carriedItem;
    public boolean restored;

    public ClientPlayerData(PlayerEntity player, UUID playerUuid, ItemStack carriedItem, boolean restored) {
        this.player = player;
        this.playerUuid = playerUuid;
        this.carriedItem = carriedItem;
        this.restored = restored;
    }

    public void rewindClientPlayerData(ClientPlayerData clientPlayerData) {
        if (clientPlayerData.restored) return;
        PlayerEntity player = clientPlayerData.player;
        InventorySaver.loadCarriedItem(player, clientPlayerData.carriedItem);
        clientPlayerData.restored = true;
    }

    public static ClientPlayerData saveClientPlayerData(PlayerEntity entity) {
        return new ClientPlayerData(
            entity,
            entity.getUUID(),
            InventorySaver.saveCarriedItem(entity), 
            false
        );
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUUID(playerUuid);
        buf.writeBoolean(restored);
        buf.writeItem(carriedItem);
    }

    public static ClientPlayerData fromBytes(PacketBuffer buf, PlayerEntity player, UUID playerUuid) {
        // UUID playerUuid = buf.readUUID();
        boolean restored = buf.readBoolean();
        ItemStack carriedItem = buf.readItem();
        return new ClientPlayerData(player, playerUuid, carriedItem, restored);
    }
}
