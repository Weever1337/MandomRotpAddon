package uk.meow.weever.rotp_mandom.client.render.item;

import com.github.standobyte.jojo.util.mc.MCUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;

public class RingoLayerModel <T extends LivingEntity> extends PlayerModel<T> {

    private final ModelRenderer arrow;
    private final ModelRenderer arrow2;

    public RingoLayerModel(float p_i46304_1_, boolean p_i46304_2_) {
        super(p_i46304_1_, p_i46304_2_);
        texWidth = 64;
        texHeight = 64;

        rightArm.texOffs(23, 2).addBox(-3.5F, 6.75F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F, true);
        rightArm.texOffs(2, 0).addBox(-3.25F, 7.0F, -2.25F, 4.5F, 1.5F, 4.5F, 0.0F, false);

        arrow2 = new ModelRenderer(this);
        arrow2.setPos(-4.498F, 7.75F, -0.001F);
        rightArm.addChild(arrow2);
        arrow2.texOffs(1, -1).addBox(0.898F, -0.37F, -0.874F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        leftArm.texOffs(23, 2).addBox(3.0F, 6.75F, -1.0F, 0.5F, 2.0F, 2.0F, 0.0F, false);
        leftArm.texOffs(2, 0).addBox(-1.25F, 7.0F, -2.25F, 4.5F, 1.5F, 4.5F, 0.0F, false);

        arrow = new ModelRenderer(this);
        arrow.setPos(3.502F, 7.75F, -0.001F);
        leftArm.addChild(arrow);
        arrow.texOffs(1, -1).addBox(0.0F, -0.37F, -0.874F, 0.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ticks, float yRot, float xRot) {
        arrow.xRot = (0.5F - ticks)/40;
        arrow2.xRot = (0.5F + ticks)/40;
    }

    public void setupClockPosition(LivingEntity entity, boolean leftHand, boolean rightHand) {
        HandSide side = MCUtil.getHandSide(entity, Hand.MAIN_HAND);
        if (side == HandSide.RIGHT) {
            leftArm.visible = leftHand;
            rightArm.visible = rightHand;
        } else {
            leftArm.visible = rightHand;
            rightArm.visible = leftHand;
        }
    }
    
    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        leftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}