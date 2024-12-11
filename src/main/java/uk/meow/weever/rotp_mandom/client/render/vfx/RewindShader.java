package uk.meow.weever.rotp_mandom.client.render.vfx;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.ResourceLocation;

public class RewindShader {
    private static Minecraft mc = Minecraft.getInstance();
    private static boolean isShaderLoaded = false;

    public static void enableShader(ResourceLocation shaderTexture) {
        if (!isShaderLoaded) {
            RenderSystem.recordRenderCall(() -> {
                mc.gameRenderer.loadEffect(shaderTexture);
            });
            isShaderLoaded = true;
        }
    }

    public static void shutdownShader() {
        ((GameRenderer) (Object) mc.gameRenderer).shutdownEffect();
        isShaderLoaded = false;
    }
}
