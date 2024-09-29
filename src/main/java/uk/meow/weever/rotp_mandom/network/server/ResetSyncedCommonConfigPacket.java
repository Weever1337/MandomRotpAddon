package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import uk.meow.weever.rotp_mandom.MandomConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetSyncedCommonConfigPacket {
    public ResetSyncedCommonConfigPacket() {
    }

    public static class Handler implements IModPacketHandler<ResetSyncedCommonConfigPacket> {

        @Override
        public void encode(ResetSyncedCommonConfigPacket msg, PacketBuffer buf) {
        }

        @Override
        public ResetSyncedCommonConfigPacket decode(PacketBuffer buf) {
            return new ResetSyncedCommonConfigPacket();
        }

        @Override
        public void handle(ResetSyncedCommonConfigPacket msg, Supplier<NetworkEvent.Context> ctx) {
            MandomConfig.Common.SyncedValues.resetConfig();
        }

        @Override
        public Class<ResetSyncedCommonConfigPacket> getPacketClass() {
            return ResetSyncedCommonConfigPacket.class;
        }
    }
}