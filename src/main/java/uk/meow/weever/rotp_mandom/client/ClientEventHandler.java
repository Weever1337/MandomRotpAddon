package uk.meow.weever.rotp_mandom.client;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.github.standobyte.jojo.util.mc.reflection.ClientReflection;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import uk.meow.weever.rotp_mandom.capability.entity.ClientPlayerEntityUtilCap;
import uk.meow.weever.rotp_mandom.capability.entity.ClientPlayerEntityUtilCapProvider;
import uk.meow.weever.rotp_mandom.item.RingoClock;

public class ClientEventHandler {
    private static ClientEventHandler instance = null;

    private final Minecraft mc;

    private ClientEventHandler(Minecraft mc) {
        this.mc = mc;
    }

    public static void init(Minecraft mc) {
        if (instance == null) {
            instance = new ClientEventHandler(mc);
            MinecraftForge.EVENT_BUS.register(instance);
        }
    }

    private boolean modPostedEvent = false;
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderHand(RenderHandEvent event) {
        ClientPlayerEntity player = mc.player;
        if (!event.isCanceled() && !modPostedEvent && !(player.hasEffect(Effects.INVISIBILITY) || player.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()) || player.isInvisible())) {
            ItemStack stack = player.getItemInHand(event.getHand());
            if (event.getHand() == Hand.MAIN_HAND && !stack.isEmpty() && stack.getItem() instanceof RingoClock) {
                renderHand(Hand.MAIN_HAND, event.getMatrixStack(), event.getBuffers(), event.getLight(),
                        event.getPartialTicks(), event.getInterpolatedPitch(), player);
                event.setCanceled(true);
            }
            if (event.getHand() == Hand.OFF_HAND && !stack.isEmpty() && stack.getItem() instanceof RingoClock) {
                renderHand(Hand.OFF_HAND, event.getMatrixStack(), event.getBuffers(), event.getLight(),
                        event.getPartialTicks(), event.getInterpolatedPitch(), player);
                event.setCanceled(true);
            }
        }
    }

    private void renderHand(Hand hand, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light,
                            float partialTick, float interpolatedPitch, LivingEntity entity) {
        FirstPersonRenderer renderer = mc.getItemInHandRenderer();
        ClientPlayerEntity player = mc.player;
        Hand swingingArm = MoreObjects.firstNonNull(player.swingingArm, Hand.MAIN_HAND);
        float swingProgress = swingingArm == hand ? player.getAttackAnim(partialTick) : 0.0F;
        float equipProgress = hand == Hand.MAIN_HAND ?
                1.0F - MathHelper.lerp(partialTick, ClientReflection.getMainHandHeightPrev(renderer), ClientReflection.getMainHandHeight(renderer))
                : 1.0F - MathHelper.lerp(partialTick, ClientReflection.getOffHandHeightPrev(renderer), ClientReflection.getOffHandHeight(renderer));

        modPostedEvent = true;
        if (!ForgeHooksClient.renderSpecificFirstPersonHand(hand,
                matrixStack, buffer, light,
                partialTick, interpolatedPitch,
                swingProgress, equipProgress, entity.getItemInHand(hand))) {
            HandSide handSide = MCUtil.getHandSide(player, hand);

            matrixStack.pushPose();
            ClientReflection.renderPlayerArm(matrixStack, buffer, light, equipProgress,
                    swingProgress, handSide, renderer);
            matrixStack.popPose();
        }
        modPostedEvent = false;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if (mc.player != null) {
            // System.out.println(mc.player.level.isClientSide() + " | " + mc.player.inventory.getCarried());
            // TODO: Add a tick event:
            mc.player.getCapability(ClientPlayerEntityUtilCapProvider.CAPABILITY).ifPresent(ClientPlayerEntityUtilCap::tick);
        }
    }
}
