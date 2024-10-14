package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class RWSetSelectedSlot {
    private final int entityId;
    private int slot = 0;


    public RWSetSelectedSlot(int entityId, int slot) {
        this.entityId = entityId;
        this.slot = slot;
    }

    public static class Handler implements IModPacketHandler<RWSetSelectedSlot> {

        @Override
        public void encode(RWSetSelectedSlot msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeInt(msg.slot);
        }

        @Override
        public RWSetSelectedSlot decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new RWSetSelectedSlot(entityId, buf.readInt());
        }

        @Override
        public void handle(RWSetSelectedSlot msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity)entity).inventory.selected = msg.slot;
            }
        }

        public Class<RWSetSelectedSlot> getPacketClass() {
            return RWSetSelectedSlot.class;
        }
    }
}
