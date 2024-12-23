package uk.meow.weever.rotp_mandom.data.entity;

import java.util.UUID;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import uk.meow.weever.rotp_mandom.data.global.InventorySaver;

public class ClientPlayerData {
    public PlayerEntity player;
    public UUID playerUuid;
    private final ItemStack carriedItem;
    private final int selectedSlot;
    public boolean restored;

    public ClientPlayerData(PlayerEntity player, UUID playerUuid, ItemStack carriedItem, int selectedSlot, boolean restored) {
        this.player = player;
        this.playerUuid = playerUuid;
        this.carriedItem = carriedItem;
        this.selectedSlot = selectedSlot;
        this.restored = restored;
    }

    public static void rewindClientPlayerData(ClientPlayerData clientPlayerData) {
        if (clientPlayerData.restored) return;
        PlayerEntity player = clientPlayerData.player;

        InventorySaver.loadSelectedSlot(player, clientPlayerData.selectedSlot);
        InventorySaver.loadCarriedItem(player, clientPlayerData.carriedItem);

        clientPlayerData.restored = true;
    }

    public static ClientPlayerData saveClientPlayerData(PlayerEntity entity) {
        return new ClientPlayerData(
            entity,
            entity.getUUID(),
            InventorySaver.saveCarriedItem(entity),
            InventorySaver.saveSelectedSlot(entity),
            false
        );
    }
}
