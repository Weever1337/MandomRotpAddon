package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import uk.meow.weever.rotp_mandom.capability.PlayerUtilCapProvider;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TrSetDataIsEmptyPacket {
    private final int entityId;
    private final boolean set;

    public TrSetDataIsEmptyPacket(int entityId, boolean set) {
        this.entityId = entityId;
        this.set = set;
    }

    public static class Handler implements IModPacketHandler<TrSetDataIsEmptyPacket> {

        @Override
        public void encode(TrSetDataIsEmptyPacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeBoolean(msg.set);
        }

        @Override
        public TrSetDataIsEmptyPacket decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new TrSetDataIsEmptyPacket(entityId, buf.readBoolean());
        }

        @Override
        public void handle(TrSetDataIsEmptyPacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity != null) {
                entity.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setDataIsEmpty(msg.set));
            }
        }

        @Override
        public Class<TrSetDataIsEmptyPacket> getPacketClass() {
            return TrSetDataIsEmptyPacket.class;
        }
    }
}
