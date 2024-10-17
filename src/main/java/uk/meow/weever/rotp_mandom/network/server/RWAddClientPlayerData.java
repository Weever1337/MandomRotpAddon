package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.client.RWSyncClientPlayerDataFromClient;

public class RWAddClientPlayerData {
    private final int entityId;
    private final int rewinderId;

    public RWAddClientPlayerData(int entityId, int rewinderId) {
        this.entityId = entityId;
        this.rewinderId = rewinderId;
    }

    public static class Handler implements IModPacketHandler<RWAddClientPlayerData> {

        @Override
        public void encode(RWAddClientPlayerData msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeInt(msg.rewinderId);
        }

        @Override
        public RWAddClientPlayerData decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new RWAddClientPlayerData(entityId, buf.readInt());
        }

        @Override
        public void handle(RWAddClientPlayerData msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            Entity rewinder = ClientUtil.getEntityById(msg.rewinderId);
            if (entity instanceof PlayerEntity && rewinder instanceof PlayerEntity) {
                ClientPlayerData clientPlayerData = ClientPlayerData.saveClientPlayerData((PlayerEntity) entity);
                AddonPackets.sendToServer(new RWSyncClientPlayerDataFromClient(msg.entityId, msg.rewinderId, clientPlayerData));
            }
        }

        public Class<RWAddClientPlayerData> getPacketClass() {
            return RWAddClientPlayerData.class;
        }
    }
}
