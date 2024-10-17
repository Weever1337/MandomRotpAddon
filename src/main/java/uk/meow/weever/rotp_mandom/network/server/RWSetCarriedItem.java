package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class RWSetCarriedItem {
    private final int entityId;
    private ItemStack carriedItem;


    public RWSetCarriedItem(int entityId, ItemStack carriedItem) {
        this.entityId = entityId;
        this.carriedItem = carriedItem;
    }

    public static class Handler implements IModPacketHandler<RWSetCarriedItem> {

        @Override
        public void encode(RWSetCarriedItem msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeItem(msg.carriedItem);
        }

        @Override
        public RWSetCarriedItem decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new RWSetCarriedItem(entityId, buf.readItem());
        }

        @Override
        public void handle(RWSetCarriedItem msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity)entity).inventory.setCarried(msg.carriedItem);
            }
        }

        public Class<RWSetCarriedItem> getPacketClass() {
            return RWSetCarriedItem.class;
        }
    }
}
