package uk.meow.weever.rotp_mandom.client.render.item;

import com.github.standobyte.jojo.client.playeranim.PlayerAnimationHandler;
import com.github.standobyte.jojo.client.render.entity.layerrenderer.IFirstPersonHandLayer;
import com.github.standobyte.jojo.init.ModStatusEffects;
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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.util.RewindSystem;

import java.util.HashMap;
import java.util.Map;

public class RingoLayerRenderer <T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> {
    private static final Map<PlayerRenderer, RingoLayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> RENDERER_LAYERS = new HashMap<>();
    private static final ResourceLocation TEXTURE = new ResourceLocation(MandomAddon.MOD_ID, "textures/item/ringo_layer.png");
    private final M ringoModel;

    public RingoLayerRenderer(IEntityRenderer<T, M> renderer, M mmm) {
        super(renderer);
        ringoModel = mmm;
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
        M playerModel = getParentModel();
        ringoModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        playerModel.copyPropertiesTo(ringoModel);
        ringoModel.setupAnim(entity, limbSwing, limbSwingAmount, ticks, yRot, xRot);
        ((RingoLayerModel) ringoModel).setupClockPosition(entity, RewindSystem.getRingoClock(entity, false, Hand.OFF_HAND), RewindSystem.getRingoClock(entity, false, Hand.MAIN_HAND));
        IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(TEXTURE), false, false);
        ringoModel.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    // @Override
    // public void renderHandFirstPerson(HandSide side, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light,
    //         AbstractClientPlayerEntity player, PlayerRenderer playerRenderer) {
    //     if (!(player.hasEffect(Effects.INVISIBILITY) || player.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()) || player.isInvisible())) {
    //         boolean isRingo = RewindSystem.getRingoClock(player, false, Hand.MAIN_HAND) || RewindSystem.getRingoClock(player, false, Hand.OFF_HAND);
    //         if (!isRingo) {
    //             return;
    //         }
    //         PlayerModel<AbstractClientPlayerEntity> model = (PlayerModel<AbstractClientPlayerEntity>) ringoModel;
    //         IFirstPersonHandLayer.defaultRender(side, matrixStack, buffer, light, player, playerRenderer, model, TEXTURE); // FIXME короче рандомно пропадают некоторые части от часов, фикситься через ф5, я не ебу как это лол
    //     }
    // }
}
