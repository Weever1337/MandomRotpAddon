package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import java.util.function.Supplier;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.util.CapabilityUtil;

public class RWRewindClientPlayerData {
    private final int entityId;

    public RWRewindClientPlayerData(int entityId) {
        this.entityId = entityId;
    }

    public static class Handler implements IModPacketHandler<RWRewindClientPlayerData> {

        @Override
        public void encode(RWRewindClientPlayerData msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
        }

        @Override
        public RWRewindClientPlayerData decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new RWRewindClientPlayerData(entityId);
        }

        @Override
        public void handle(RWRewindClientPlayerData msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof AbstractClientPlayerEntity) {
                // TODO: Add a Rewind feature
                ClientPlayerData clientPlayerData = CapabilityUtil.getClientPlayerData((PlayerEntity) entity).getFirst();
                ClientPlayerData.rewindClientPlayerData(clientPlayerData);
            }
        }

        public Class<RWRewindClientPlayerData> getPacketClass() {
            return RWRewindClientPlayerData.class;
        }
    }
}
