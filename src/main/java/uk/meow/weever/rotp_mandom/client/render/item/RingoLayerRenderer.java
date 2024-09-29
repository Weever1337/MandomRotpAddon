package uk.meow.weever.rotp_mandom.client.render.item;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.playeranim.PlayerAnimationHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.item.RingoClock;
import uk.meow.weever.rotp_mandom.util.RewindSystem;

import java.util.HashMap;
import java.util.Map;

public class RingoLayerRenderer <T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {
    private static final Map<PlayerRenderer, RingoLayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> RENDERER_LAYERS = new HashMap<>();
    private static final ResourceLocation TEXTURE = new ResourceLocation(MandomAddon.MOD_ID, "textures/item/ringo_layer.png");
    private final RingoLayerModel<T> mandomArchive = new RingoLayerModel<>(0.3f, true);
    private boolean playerAnimHandled = false;

    public RingoLayerRenderer(IEntityRenderer<T, M> renderer) {
        super(renderer);
        if (renderer instanceof PlayerRenderer) {
            RENDERER_LAYERS.put((PlayerRenderer) renderer, (RingoLayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>) this);
        }
        PlayerAnimationHandler.getPlayerAnimator().onArmorLayerInit(this);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ticks, float yRot, float xRot) {
        boolean isRingo = RewindSystem.getRingoClock(entity, false, Hand.MAIN_HAND) || RewindSystem.getRingoClock(entity, false, Hand.OFF_HAND);
        if (!isRingo) {
            return;
        }
        if (!playerAnimHandled) {
            PlayerAnimationHandler.getPlayerAnimator().onArmorLayerInit(this);
            playerAnimHandled = true;
        }
        M playerModel = getParentModel();
        mandomArchive.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        playerModel.copyPropertiesTo(mandomArchive);
        mandomArchive.setupAnim(entity, limbSwing, limbSwingAmount, ticks, yRot, xRot);
        mandomArchive.setupClockPosition(entity, RewindSystem.getRingoClock(entity, false, Hand.OFF_HAND), RewindSystem.getRingoClock(entity, false, Hand.MAIN_HAND));
        IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(TEXTURE), false, false);
        mandomArchive.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    public static void renderFirstPerson(HandSide side, MatrixStack matrixStack,
                                         IRenderTypeBuffer buffer, int light, AbstractClientPlayerEntity player) {
        EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        if (renderer instanceof PlayerRenderer) {
            PlayerRenderer playerRenderer = (PlayerRenderer) renderer;
            if (RENDERER_LAYERS.containsKey(playerRenderer)) {
                RingoLayerRenderer<?, ?> layer = RENDERER_LAYERS.get(playerRenderer);
                if (layer != null) {
                    layer.renderHandFirstPerson(side, matrixStack,
                            buffer, light, player, playerRenderer);
                }
            }
        }
    }

    private void renderHandFirstPerson(HandSide side, MatrixStack matrixStack,
                                       IRenderTypeBuffer buffer, int light, AbstractClientPlayerEntity player,
                                       PlayerRenderer playerRenderer) {
        if (player.isSpectator()) return;

        PlayerModel<AbstractClientPlayerEntity> model = playerRenderer.getModel();

        ClientUtil.setupForFirstPersonRender(model, player);
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
        light = ClientUtil.MAX_MODEL_LIGHT;
        ModelRenderer arm = ClientUtil.getArm(model, side);
        ModelRenderer armOuter = ClientUtil.getArmOuter(model, side);
        arm.xRot = 0.0F;
        arm.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY);
        armOuter.xRot = 0.0F;
        armOuter.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY);
    }

    public static ItemStack getRenderedGlovesItem(LivingEntity entity) {
        ItemStack checkedItem = entity.getMainHandItem();
        if (areGloves(checkedItem)) return checkedItem;
        checkedItem = entity.getOffhandItem();
        if (areGloves(checkedItem)) return checkedItem;
        return ItemStack.EMPTY;
    }

    public static boolean areGloves(ItemStack item) {
        return !item.isEmpty() && item.getItem() instanceof RingoClock;
    }
}
