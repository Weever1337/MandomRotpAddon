package uk.meow.weever.rotp_mandom.client.render;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.standskin.StandSkinsManager;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.init.InitStands;

import java.util.concurrent.atomic.AtomicReference;

public class MandomLayerRenderer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MandomAddon.MOD_ID, "textures/entity/stand/mandom.png");
    private final MandomLayerModel<T> mandomArchive;

    public MandomLayerRenderer(IEntityRenderer<T, M> renderer) {
        super(renderer);
        this.mandomArchive = new MandomLayerModel<>(0);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ticks, float yRot, float xRot) {
        if (!ClientUtil.canSeeStands()) {
            return;
        }
        IStandPower.getStandPowerOptional(entity).ifPresent((stand) -> {
            StandType<?> mandom = InitStands.MANDOM.getStandType();
            if (stand.getType() == mandom && stand.isActive()) {
                M entityModel = getParentModel();
                mandomArchive.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
                mandomArchive.copyPropertiesTo(entityModel);
                mandomArchive.setupAnim(entity, limbSwing, limbSwingAmount, ticks, yRot, xRot);

                IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.entityTranslucent(getTexture(entity)), false, false);
                mandomArchive.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        });
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