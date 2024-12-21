package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.util.AddonUtil;

import java.util.LinkedList;

public class ClientPlayerEntityUtilCap {
    private final PlayerEntity playerEntity;
    private final LinkedList<ClientPlayerData> clientPlayerEntityData = new LinkedList<>();

    public ClientPlayerEntityUtilCap(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public void tick() {
        if (playerEntity.tickCount % 20 == 0) {
            int maxSize = RewindConfig.getSecond();

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
