package uk.meow.weever.rotp_mandom.data.entity;

import java.util.UUID;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import uk.meow.weever.rotp_mandom.data.global.InventorySaver;

public class ClientPlayerData {
    public AbstractClientPlayerEntity player;
    public UUID playerUuid;
    private final ItemStack carriedItem;
    public boolean restored;

    public ClientPlayerData(AbstractClientPlayerEntity player, UUID playerUuid, ItemStack carriedItem, boolean restored) {
        this.player = player;
        this.playerUuid = playerUuid;
        this.carriedItem = carriedItem;
        this.restored = restored;
    }

    public static void rewindClientPlayerData(ClientPlayerData clientPlayerData) {
        if (clientPlayerData.restored) return;
        AbstractClientPlayerEntity player = clientPlayerData.player;
        // System.out.println(clientPlayerData.carriedItem.getDisplayName().getString());
        InventorySaver.loadCarriedItem(player, clientPlayerData.carriedItem);
        clientPlayerData.restored = true;
    }

    public static ClientPlayerData saveClientPlayerData(AbstractClientPlayerEntity entity) {
        // System.out.println(InventorySaver.saveCarriedItem(entity));
        return new ClientPlayerData(
            entity,
            entity.getUUID(),
            InventorySaver.saveCarriedItem(entity), 
            false
        );
    }
}
