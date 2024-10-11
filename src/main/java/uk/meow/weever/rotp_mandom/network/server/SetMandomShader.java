package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import uk.meow.weever.rotp_mandom.MandomAddon;

public class SetMandomShader {
    private final int entityId;
    private static final ResourceLocation MANDOM_SHADER = new ResourceLocation(MandomAddon.MOD_ID, "shaders/post/mandom.json");
    private boolean set = true;


    public SetMandomShader(int entityId, boolean set) {
        this.entityId = entityId;
        this.set = set;
    }

    public static class Handler implements IModPacketHandler<SetMandomShader> {

        @Override
        public void encode(SetMandomShader msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeBoolean(msg.set);
        }

        @Override
        public SetMandomShader decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new SetMandomShader(entityId, buf.readBoolean());
        }

        @Override
        public void handle(SetMandomShader msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof PlayerEntity) {
                Minecraft mc = Minecraft.getInstance();
                if (msg.set) {
                    RenderSystem.recordRenderCall(() -> {
                        ((GameRenderer)(Object)mc.gameRenderer).loadEffect(MANDOM_SHADER);
                    });
                } else {
                    mc.gameRenderer.shutdownEffect();
                }
            }
        }

        public Class<SetMandomShader> getPacketClass() {
            return SetMandomShader.class;
        }
    }
}
