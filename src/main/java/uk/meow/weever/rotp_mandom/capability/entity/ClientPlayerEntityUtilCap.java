package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;

import java.util.LinkedList;

public class ClientPlayerEntityUtilCap {
    private final AbstractClientPlayerEntity playerEntity;
    private final LinkedList<ClientPlayerData> clientPlayerEntityData = new LinkedList<>();

    public ClientPlayerEntityUtilCap(AbstractClientPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public void tick() {
        if (playerEntity.tickCount % 20 == 0) {
            int maxSize = RewindConfig.getSecond(playerEntity.level.isClientSide());

            if (this.clientPlayerEntityData.size() > maxSize) {
                this.clientPlayerEntityData.removeFirst();
            }
            addClientPlayerData(ClientPlayerData.saveClientPlayerData(playerEntity), maxSize);
        }
    }

    public LinkedList<ClientPlayerData> getClientPlayerData() {
        return clientPlayerEntityData;
    }

    public void addClientPlayerData(ClientPlayerData cData, int maxSize) {
        if (this.clientPlayerEntityData.size() > maxSize) {
            this.clientPlayerEntityData.removeFirst();
        }
        this.clientPlayerEntityData.addLast(cData);
    }
}
