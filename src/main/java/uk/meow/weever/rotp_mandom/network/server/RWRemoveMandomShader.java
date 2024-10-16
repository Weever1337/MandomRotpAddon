package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class RWRemoveMandomShader {
    private final int entityId;

    public RWRemoveMandomShader(int entityId) {
        this.entityId = entityId;
    }

    public static class Handler implements IModPacketHandler<RWRemoveMandomShader> {

        @Override
        public void encode(RWRemoveMandomShader msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
        }

        @Override
        public RWRemoveMandomShader decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new RWRemoveMandomShader(entityId);
        }

        @Override
        public void handle(RWRemoveMandomShader msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof PlayerEntity) {
                Minecraft mc = Minecraft.getInstance();
                mc.gameRenderer.shutdownEffect();
            }
        }

        public Class<RWRemoveMandomShader> getPacketClass() {
            return RWRemoveMandomShader.class;
        }
    }
}
