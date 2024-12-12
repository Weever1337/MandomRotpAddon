package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import uk.meow.weever.rotp_mandom.client.render.vfx.RewindShader;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.util.CapabilityUtil;

import java.util.function.Supplier;

public class RWRemoveShader {
    private final int entityId;

    public RWRemoveShader(int entityId) {
        this.entityId = entityId;
    }

    public static class Handler implements IModPacketHandler<RWRemoveShader> {

        @Override
        public void encode(RWRemoveShader msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
        }

        @Override
        public RWRemoveShader decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new RWRemoveShader(entityId);
        }

        @Override
        public void handle(RWRemoveShader msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof AbstractClientPlayerEntity) {
                RewindShader.shutdownShader();
            }
        }

        public Class<RWRemoveShader> getPacketClass() {
            return RWRemoveShader.class;
        }
    }
}
