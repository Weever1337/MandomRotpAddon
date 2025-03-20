package uk.meow.weever.rotp_mandom.client.render.item;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.playeranim.PlayerAnimationHandler;
import com.github.standobyte.jojo.client.render.entity.layerrenderer.IFirstPersonHandLayer;
import com.github.standobyte.jojo.client.standskin.StandSkinsManager;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.init.InitStands;
import uk.meow.weever.rotp_mandom.item.RingoClock;
import uk.meow.weever.rotp_mandom.util.RewindSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class RingoLayerRenderer <T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> implements IFirstPersonHandLayer {
    private static final Map<PlayerRenderer, RingoLayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> RENDERER_LAYERS = new HashMap<>();
    private static final ResourceLocation TEXTURE = new ResourceLocation(MandomAddon.MOD_ID, "textures/item/ringo_layer.png");
    private final M ringoModel;
    private boolean playerAnimHandled = false;

    public RingoLayerRenderer(IEntityRenderer<T, M> renderer, M mmm) {
        super(renderer);
        ringoModel = mmm;
        if (renderer instanceof PlayerRenderer) {
            RENDERER_LAYERS.put((PlayerRenderer) renderer, (RingoLayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>) this);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ticks, float yRot, float xRot) {

        boolean isRingo = RewindSystem.getRingoClock(entity);
        if (!isRingo) {
            return;
        }

        if (!playerAnimHandled) {
            PlayerAnimationHandler.getPlayerAnimator().onArmorLayerInit(this);
            playerAnimHandled = true;
        }
        M playerModel = getParentModel();
        ringoModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        playerModel.copyPropertiesTo(ringoModel);
        ringoModel.setupAnim(entity, limbSwing, limbSwingAmount, ticks, yRot, xRot);
        ringoModel.leftArm.visible = playerModel.leftArm.visible;
        ringoModel.rightArm.visible = playerModel.rightArm.visible;

        ((RingoLayerModel<?>) ringoModel).setupClockPosition(entity, RewindSystem.getRingoClock(entity, false, Hand.OFF_HAND), RewindSystem.getRingoClock(entity, false, Hand.MAIN_HAND));
        IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(TEXTURE), false, false);
        ringoModel.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Override
    public void renderHandFirstPerson(HandSide side, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light,
                                      AbstractClientPlayerEntity player, PlayerRenderer playerRenderer) {
        if (!(player.hasEffect(Effects.INVISIBILITY) || player.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()) || player.isInvisible())) {
            boolean isRingo = RewindSystem.getRingoClock(player, side);
            ItemStack ringoClockStack = getRenderedClockItem(player);
            if (!isRingo || ringoClockStack.isEmpty()) {
                return;
            }
            PlayerModel<AbstractClientPlayerEntity> model = playerRenderer.getModel();
            ResourceLocation texture = getTexture(player);
            ClientUtil.setupForFirstPersonRender(model, player);
            IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(texture), false, false);
            ModelRenderer glove = ClientUtil.getArm(model, side);
            ModelRenderer gloveOuter = ClientUtil.getArmOuter(model, side);
            glove.xRot = 0.0F;
            glove.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY);
            gloveOuter.xRot = 0.0F;
            gloveOuter.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY);
        }
    }

    public static ItemStack getRenderedClockItem(LivingEntity entity) {
        ItemStack checkedItem = entity.getMainHandItem();
        if (areClocks(checkedItem)) return checkedItem;
        checkedItem = entity.getOffhandItem();
        if (areClocks(checkedItem)) return checkedItem;
        return ItemStack.EMPTY;
    }

    public static boolean areClocks(ItemStack item) {
        return !item.isEmpty() && item.getItem() instanceof RingoClock;
    }

    private ResourceLocation getTexture(LivingEntity entity) {
        AtomicReference<ResourceLocation> texture = new AtomicReference<>(TEXTURE);
        IStandPower.getStandPowerOptional(entity).ifPresent((stand) -> {
            StandType<?> mandom = InitStands.MANDOM.getStandType();
            if (stand.getType() == mandom && stand.isActive()) {
                texture.set(StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
                        .getStandSkin(stand.getStandInstance().get()), TEXTURE));
            }
        });
        return texture.get();
    }
}
