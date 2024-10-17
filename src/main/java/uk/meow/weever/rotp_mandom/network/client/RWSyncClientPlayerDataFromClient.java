package uk.meow.weever.rotp_mandom.network.client;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import uk.meow.weever.rotp_mandom.data.entity.ClientPlayerData;
import uk.meow.weever.rotp_mandom.util.CapabilityUtil;

public class RWSyncClientPlayerDataFromClient {
    private final int entityId;
    private final int rewinderId;
    private final ClientPlayerData clientPlayerData;

    public RWSyncClientPlayerDataFromClient(int entityId, int rewinderId, ClientPlayerData clientPlayerData) {
        this.entityId = entityId;
        this.rewinderId = rewinderId;
        this.clientPlayerData = clientPlayerData;
    }

    public static class Handler implements IModPacketHandler<RWSyncClientPlayerDataFromClient> {

        @Override
        public void encode(RWSyncClientPlayerDataFromClient msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeInt(msg.rewinderId);
            msg.clientPlayerData.toBytes(buf);
        }

        @Override
        public RWSyncClientPlayerDataFromClient decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            int rewinderId = buf.readInt();
            UUID playerUUID = buf.readUUID();
            
            Iterable<ServerWorld> levels = ServerLifecycleHooks.getCurrentServer().getAllLevels();
            PlayerEntity player = null;
            for (ServerWorld level : levels) {
                player = level.getPlayerByUUID(playerUUID);
                if (player != null) break;
            }
            ClientPlayerData clientPlayerData = ClientPlayerData.fromBytes(buf, player, playerUUID);
            return new RWSyncClientPlayerDataFromClient(entityId, rewinderId, clientPlayerData);
        }

        @Override
        public void handle(RWSyncClientPlayerDataFromClient msg, Supplier<NetworkEvent.Context> ctx) {
            Entity rewinder = ctx.get().getSender().getLevel().getEntity(msg.rewinderId);
            if (rewinder instanceof PlayerEntity) {
                CapabilityUtil.addClientPlayerData((PlayerEntity) rewinder, msg.clientPlayerData);
            }
        }

        public Class<RWSyncClientPlayerDataFromClient> getPacketClass() {
            return RWSyncClientPlayerDataFromClient.class;
        }
    }
}
