package uk.meow.weever.rotp_mandom.data.entity;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.config.TPARConfig;
import uk.meow.weever.rotp_mandom.data.global.InventorySaver;

public class ClientPlayerData implements IEntityData<ClientPlayerData, PlayerEntity> {
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

    public static void rewindClientPlayerData(ClientPlayerData clientPlayerData) {
        if (clientPlayerData.restored) return;
        PlayerEntity player = clientPlayerData.player;
        // System.out.println(player.getName().getString());
        if (TPARConfig.getSaveItems(player.level.isClientSide())) {
            InventorySaver.loadCarriedItem(player, clientPlayerData.carriedItem);
        }
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

    @Override
    public void rewindData(IEntityData<ClientPlayerData, PlayerEntity> data) {
        if (((ClientPlayerData) data).isRestored(data)) return;
        rewindClientPlayerData((ClientPlayerData) data);
    }

    @Override
    public void rewindDeadData(IEntityData<ClientPlayerData, PlayerEntity> data, World level) {
        return;
    }

    @Override
    public boolean isRestored(IEntityData<ClientPlayerData, PlayerEntity> data) {
        return ((ClientPlayerData) data).restored;
    }

    @Override
    public Entity getEntity(IEntityData<ClientPlayerData, PlayerEntity> data) {
        return ((ClientPlayerData) data).player;
    }

    @Override
    public boolean inData(Queue<IEntityData<ClientPlayerData, PlayerEntity>> cData, PlayerEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        cData.forEach(data -> {
            if (data.getEntity(data) == entity) {
                inData.set(true);
            }
        });
        return inData.get();
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
