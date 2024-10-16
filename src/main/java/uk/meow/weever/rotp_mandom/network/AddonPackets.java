package uk.meow.weever.rotp_mandom.network;

import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.network.server.*;

import java.util.Optional;

public class AddonPackets {
    private static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel channel;
    private static int packetIndex;

    public static void init() {
        channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MandomAddon.MOD_ID, "main_channel"))
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();

        registerMessage(channel, new CommonConfigPacket.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new ResetSyncedCommonConfigPacket.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new TrSetDataIsEmptyPacket.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new TrResetDeathTimePacket.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new RWSetMandomShader.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new RWRemoveMandomShader.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        registerMessage(channel, new RWSetSelectedSlot.Handler(), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private static <MSG> void registerMessage(SimpleChannel channel, IModPacketHandler<MSG> handler, Optional<NetworkDirection> networkDirection) {
        if (packetIndex > 127) {
            throw new IllegalStateException("Too many packets (> 127) registered for a single channel!");
        }
        channel.registerMessage(packetIndex++, handler.getPacketClass(), handler::encode, handler::decode, handler::enqueueHandleSetHandled, networkDirection);
    }

    public static void sendToClient(Object msg, ServerPlayerEntity player) {
        if (!(player instanceof FakePlayer)) {
            channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }

    public static void sendToClient(Object msg, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            sendToClient(msg, ((ServerPlayerEntity) player));
        } else {
            MandomAddon.LOGGER.warn("You can't send a message not by a player!");
        }
    }

    public static void sendToServer(Object msg) {
        channel.sendToServer(msg);
    }

    public static void sendToClientsTracking(Object msg, Entity entity) {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    public static void sendToClientsTrackingAndSelf(Object msg, Entity entity) {
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }
}
