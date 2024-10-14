package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import uk.meow.weever.rotp_mandom.MandomConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CommonConfigPacket {
    private final MandomConfig.Common.SyncedValues values;

    public CommonConfigPacket(MandomConfig.Common.SyncedValues values) {
        this.values = values;
    }

    public static class Handler implements IModPacketHandler<CommonConfigPacket> {

        @Override
        public void encode(CommonConfigPacket msg, PacketBuffer buf) {
            msg.values.writeToBuf(buf);
        }

        @Override
        public CommonConfigPacket decode(PacketBuffer buf) {
            return new CommonConfigPacket(new MandomConfig.Common.SyncedValues(buf));
        }

        @Override
        public void handle(CommonConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
            msg.values.changeConfigValues();
        }

        @Override
        public Class<CommonConfigPacket> getPacketClass() {
            return CommonConfigPacket.class;
        }
    }
}