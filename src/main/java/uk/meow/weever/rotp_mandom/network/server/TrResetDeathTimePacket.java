package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import java.util.function.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class TrResetDeathTimePacket {
    private final int entityId;


    public TrResetDeathTimePacket(int entityId) {
        this.entityId = entityId;
    }

    public static class Handler implements IModPacketHandler<TrResetDeathTimePacket> {

        @Override
        public void encode(TrResetDeathTimePacket msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
        }

        @Override
        public TrResetDeathTimePacket decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new TrResetDeathTimePacket(entityId);
        }

        @Override
        public void handle(TrResetDeathTimePacket msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof LivingEntity) {
                LivingEntity living = ((LivingEntity) entity);
                if (living.isDeadOrDying()) {
                    living.setHealth(0.0001F);
                }
                living.deathTime = 0;
            }
        }

        public Class<TrResetDeathTimePacket> getPacketClass() {
            return TrResetDeathTimePacket.class;
        }
    }
}
