package uk.meow.weever.rotp_mandom.client.render.vfx;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class RewindShader {
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean isShaderLoaded = false;
    private static int ticks;
    private static int duration = -1;
    @Nullable private static ResourceLocation shaderTexture;

    public static void enableShader(ResourceLocation shaderTextureToRender, int durationInTicks) {
        shaderTexture = shaderTextureToRender;
        setDuration(durationInTicks);
    }

    public static void shutdownShader() {
        mc.gameRenderer.shutdownEffect();
        isShaderLoaded = false;
        setDuration(-1);
        shaderTexture = null;
        ticks = 0;
    }

    public static void onShaderTick() {
        if (duration == 0) {
            shutdownShader();
        } else if (duration == -1) {
            ticks = 0;
            return;
        }
        ticks++;
        if (ticks >= duration) {
            duration--;
        }
        renderShaders();
    }

    private static void renderShaders() {
        if (!isShaderLoaded && shaderTexture != null) {
            RenderSystem.recordRenderCall(() -> mc.gameRenderer.loadEffect(shaderTexture));
            setIsShaderLoaded(true);
        }
    }

    public static void setDuration(int set) {
        duration = set;
    }

    public static void setIsShaderLoaded(boolean set) {
        isShaderLoaded = set;
    }
}
