package uk.meow.weever.rotp_mandom.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.network.packets.IModPacketHandler;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class RWSetMandomShader {
    private final int entityId;
    private final ResourceLocation shaderRes;

    public RWSetMandomShader(int entityId, ResourceLocation shaderRes) {
        this.entityId = entityId;
        this.shaderRes = shaderRes;
    }

    public static class Handler implements IModPacketHandler<RWSetMandomShader> {

        @Override
        public void encode(RWSetMandomShader msg, PacketBuffer buf) {
            buf.writeInt(msg.entityId);
            buf.writeResourceLocation(msg.shaderRes);
        }

        @Override
        public RWSetMandomShader decode(PacketBuffer buf) {
            int entityId = buf.readInt();
            return new RWSetMandomShader(entityId, buf.readResourceLocation());
        }

        @Override
        public void handle(RWSetMandomShader msg, Supplier<NetworkEvent.Context> ctx) {
            Entity entity = ClientUtil.getEntityById(msg.entityId);
            if (entity instanceof PlayerEntity) {
                // Minecraft mc = Minecraft.getInstance();

                // ShaderGroup effect;
                // try {
                //     effect = new CustomShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), msg.shaderRes);
                //     ClientReflection.setPostEffect(mc.gameRenderer, effect);
                //     effect.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
                //     ClientReflection.setEffectActive(mc.gameRenderer, true);
                // } catch (JsonSyntaxException e) {
                //     e.printStackTrace();
                // } catch (IOException e) {
                //     e.printStackTrace();
                // }
                // RenderSystem.recordRenderCall(() -> {
                //     ((GameRenderer)(Object)mc.gameRenderer).loadEffect(msg.shaderRes);
                // });
            }
        }

        public Class<RWSetMandomShader> getPacketClass() {
            return RWSetMandomShader.class;
        }
    }
}
