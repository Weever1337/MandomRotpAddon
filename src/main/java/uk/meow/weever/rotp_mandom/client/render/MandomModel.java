package uk.meow.weever.rotp_mandom.client.render;

import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;

import net.minecraft.client.renderer.model.ModelRenderer;
import uk.meow.weever.rotp_mandom.entity.MandomEntity;

public class MandomModel extends HumanoidStandModel<MandomEntity> {
    private final ModelRenderer Mandom;
    private final ModelRenderer m_head;
    private final ModelRenderer m_body;
    private final ModelRenderer m_upperPart;
    private final ModelRenderer eyes;
    private final ModelRenderer eye_r1;
    private final ModelRenderer eye_r2;
    private final ModelRenderer eye_r3;
    private final ModelRenderer eye_r4;
    private final ModelRenderer eye_r5;
    private final ModelRenderer eye_r6;
    private final ModelRenderer eye_r7;
    private final ModelRenderer eye_r8;
    private final ModelRenderer eye_r9;
    private final ModelRenderer eye_r10;
    private final ModelRenderer eye_r11;
    private final ModelRenderer eye_r12;
    private final ModelRenderer eye_r13;
    private final ModelRenderer eye_r14;
    private final ModelRenderer eye_r15;
    private final ModelRenderer eye_r16;
    private final ModelRenderer eye_r17;
    private final ModelRenderer eye_r18;
    private final ModelRenderer eye_r19;
    private final ModelRenderer eye_r20;
    private final ModelRenderer eye_r21;
    private final ModelRenderer eye_r22;
    private final ModelRenderer eye_r23;
    private final ModelRenderer shoulder;
    private final ModelRenderer shoulder_r1;
    private final ModelRenderer shoulder_r2;
    private final ModelRenderer shoulder_r3;
    private final ModelRenderer shoulder_r4;
    private final ModelRenderer shoulder_r5;
    private final ModelRenderer shoulder_r6;
    private final ModelRenderer limbs;
    private final ModelRenderer left;
    private final ModelRenderer L1;
    private final ModelRenderer L1_1;
    private final ModelRenderer L1_2;
    private final ModelRenderer L1_3;
    private final ModelRenderer L1_4;
    private final ModelRenderer paw;
    private final ModelRenderer L2;
    private final ModelRenderer L2_1;
    private final ModelRenderer L2_2;
    private final ModelRenderer L2_3;
    private final ModelRenderer L2_4;
    private final ModelRenderer paw2;
    private final ModelRenderer L3;
    private final ModelRenderer L3_1;
    private final ModelRenderer L3_2;
    private final ModelRenderer L3_3;
    private final ModelRenderer L3_4;
    private final ModelRenderer paw3;
    private final ModelRenderer L4;
    private final ModelRenderer L4_1;
    private final ModelRenderer L4_2;
    private final ModelRenderer L4_3;
    private final ModelRenderer L4_4;
    private final ModelRenderer paw4;
    private final ModelRenderer L5;
    private final ModelRenderer L5_1;
    private final ModelRenderer L5_2;
    private final ModelRenderer L5_3;
    private final ModelRenderer L5_4;
    private final ModelRenderer paw5;
    private final ModelRenderer right;
    private final ModelRenderer R1;
    private final ModelRenderer R1_2;
    private final ModelRenderer R1_3;
    private final ModelRenderer R1_4;
    private final ModelRenderer R1_5;
    private final ModelRenderer paw6;
    private final ModelRenderer R2;
    private final ModelRenderer R2_2;
    private final ModelRenderer R2_3;
    private final ModelRenderer R2_4;
    private final ModelRenderer R2_5;
    private final ModelRenderer paw7;
    private final ModelRenderer R3;
    private final ModelRenderer R3_2;
    private final ModelRenderer R3_3;
    private final ModelRenderer R3_4;
    private final ModelRenderer R3_5;
    private final ModelRenderer paw8;
    private final ModelRenderer R4;
    private final ModelRenderer R4_1;
    private final ModelRenderer R4_2;
    private final ModelRenderer R4_3;
    private final ModelRenderer R4_4;
    private final ModelRenderer paw9;
    private final ModelRenderer R5;
    private final ModelRenderer R5_1;
    private final ModelRenderer R5_2;
    private final ModelRenderer R5_3;
    private final ModelRenderer R5_4;
    private final ModelRenderer paw10;


    public MandomModel() {
        super();
        texWidth = 128;
        texHeight = 128;

        Mandom = new ModelRenderer(this);
        Mandom.setPos(-6.5F, -1F, 0.0F);
        rightArm.addChild(Mandom);
        setRotationAngle(Mandom, 0.994F, -6.6493F, -0.6179F);


        m_head = new ModelRenderer(this);
        m_head.setPos(0.2616F, -4.4867F, 1.2377F);
        Mandom.addChild(m_head);
        setRotationAngle(m_head, -0.6883F, 0.2457F, 0.2028F);
        m_head.texOffs(7, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        eyes = new ModelRenderer(this);
        eyes.setPos(15.0F, 9.85F, 0.0F);
        m_head.addChild(eyes);
        eyes.texOffs(22, 54).addBox(-15.25F, -13.25F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eyes.texOffs(22, 57).addBox(-15.25F, -11.25F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eyes.texOffs(22, 60).addBox(-15.25F, -9.3F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eyes.texOffs(22, 63).addBox(-15.25F, -7.2F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eyes.texOffs(17, 60).addBox(-17.5F, -10.2F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eyes.texOffs(27, 63).addBox(-13.0F, -8.1F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eyes.texOffs(27, 57).addBox(-13.0F, -12.15F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eyes.texOffs(27, 60).addBox(-13.0F, -10.2F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eyes.texOffs(17, 57).addBox(-17.5F, -12.15F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eyes.texOffs(17, 63).addBox(-17.5F, -8.1F, -4.25F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r1 = new ModelRenderer(this);
        eye_r1.setPos(-13.0F, -6.25F, -3.95F);
        eyes.addChild(eye_r1);
        setRotationAngle(eye_r1, 0.7854F, 0.0F, 0.0F);
        eye_r1.texOffs(12, 51).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r1.texOffs(32, 51).addBox(-4.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r2 = new ModelRenderer(this);
        eye_r2.setPos(-15.1F, -9.597F, 3.8263F);
        eyes.addChild(eye_r2);
        setRotationAngle(eye_r2, -3.1416F, 0.0F, 3.1416F);
        eye_r2.texOffs(62, 57).addBox(-0.15F, -0.703F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r2.texOffs(62, 54).addBox(-0.15F, -2.703F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r2.texOffs(62, 60).addBox(-0.15F, 1.247F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r2.texOffs(57, 63).addBox(-2.6F, 2.347F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r2.texOffs(57, 57).addBox(-2.6F, -1.703F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r2.texOffs(57, 60).addBox(-2.6F, 0.247F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r2.texOffs(67, 60).addBox(2.1F, 0.347F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r2.texOffs(67, 57).addBox(2.1F, -3.603F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r2.texOffs(57, 57).addBox(-2.6F, -3.703F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r2.texOffs(67, 57).addBox(2.1F, -1.603F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r2.texOffs(67, 63).addBox(2.1F, 2.447F, -0.4763F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r3 = new ModelRenderer(this);
        eye_r3.setPos(-15.2F, -6.1439F, 3.75F);
        eyes.addChild(eye_r3);
        setRotationAngle(eye_r3, -2.3562F, 0.0F, -3.1416F);
        eye_r3.texOffs(62, 63).addBox(-0.25F, -0.25F, -0.5F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r4 = new ModelRenderer(this);
        eye_r4.setPos(-18.9F, -12.3F, -1.55F);
        eyes.addChild(eye_r4);
        setRotationAngle(eye_r4, 0.0F, 1.5708F, 0.0F);
        eye_r4.texOffs(7, 54).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r4.texOffs(7, 57).addBox(-0.5F, 2.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r4.texOffs(7, 60).addBox(-0.5F, 3.95F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r5 = new ModelRenderer(this);
        eye_r5.setPos(-18.9F, -6.25F, 3.05F);
        eyes.addChild(eye_r5);
        setRotationAngle(eye_r5, 0.0F, 1.5708F, -0.7854F);
        eye_r5.texOffs(2, 63).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r6 = new ModelRenderer(this);
        eye_r6.setPos(-18.9F, -8.35F, 3.05F);
        eyes.addChild(eye_r6);
        setRotationAngle(eye_r6, 0.0F, 1.5708F, 0.0F);
        eye_r6.texOffs(2, 60).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r6.texOffs(2, 57).addBox(-0.5F, -1.95F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r6.texOffs(2, 54).addBox(-0.5F, -3.95F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r7 = new ModelRenderer(this);
        eye_r7.setPos(-18.9F, -6.25F, -1.55F);
        eyes.addChild(eye_r7);
        setRotationAngle(eye_r7, 0.0F, 1.5708F, -0.7854F);
        eye_r7.texOffs(7, 63).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r8 = new ModelRenderer(this);
        eye_r8.setPos(-18.9F, -13.15F, -3.75F);
        eyes.addChild(eye_r8);
        setRotationAngle(eye_r8, 0.0F, 1.5708F, 0.0F);
        eye_r8.texOffs(37, 54).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r8.texOffs(37, 57).addBox(-0.5F, 2.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r8.texOffs(37, 60).addBox(-0.5F, 3.95F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r8.texOffs(37, 63).addBox(-0.5F, 6.05F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r9 = new ModelRenderer(this);
        eye_r9.setPos(-18.9F, -13.15F, 0.75F);
        eyes.addChild(eye_r9);
        setRotationAngle(eye_r9, 0.0F, 1.5708F, 0.0F);
        eye_r9.texOffs(12, 54).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r9.texOffs(12, 57).addBox(-0.5F, 2.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r9.texOffs(12, 60).addBox(-0.5F, 3.95F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r9.texOffs(12, 63).addBox(-0.5F, 6.05F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r10 = new ModelRenderer(this);
        eye_r10.setPos(-11.1F, -6.3F, -1.45F);
        eyes.addChild(eye_r10);
        setRotationAngle(eye_r10, 0.0F, -1.5708F, 0.7854F);
        eye_r10.texOffs(47, 63).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r11 = new ModelRenderer(this);
        eye_r11.setPos(-11.1F, -8.3F, -1.45F);
        eyes.addChild(eye_r11);
        setRotationAngle(eye_r11, 0.0F, -1.5708F, 0.0F);
        eye_r11.texOffs(47, 60).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r11.texOffs(47, 57).addBox(0.0F, -1.95F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r11.texOffs(47, 54).addBox(0.0F, -3.95F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r12 = new ModelRenderer(this);
        eye_r12.setPos(-11.35F, -13.9F, 3.3F);
        eyes.addChild(eye_r12);
        setRotationAngle(eye_r12, 0.0F, -1.5708F, -0.7854F);
        eye_r12.texOffs(52, 51).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r13 = new ModelRenderer(this);
        eye_r13.setPos(-18.65F, -13.8F, 2.95F);
        eyes.addChild(eye_r13);
        setRotationAngle(eye_r13, 0.0F, 1.5708F, 0.7854F);
        eye_r13.texOffs(2, 51).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r14 = new ModelRenderer(this);
        eye_r14.setPos(-18.65F, -13.8F, -1.55F);
        eyes.addChild(eye_r14);
        setRotationAngle(eye_r14, 0.0F, 1.5708F, 0.7854F);
        eye_r14.texOffs(7, 51).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r15 = new ModelRenderer(this);
        eye_r15.setPos(-11.35F, -13.9F, -1.45F);
        eyes.addChild(eye_r15);
        setRotationAngle(eye_r15, 0.0F, -1.5708F, -0.7854F);
        eye_r15.texOffs(47, 51).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r16 = new ModelRenderer(this);
        eye_r16.setPos(-11.1F, -12.25F, 3.3F);
        eyes.addChild(eye_r16);
        setRotationAngle(eye_r16, 0.0F, -1.5708F, 0.0F);
        eye_r16.texOffs(52, 54).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r16.texOffs(52, 57).addBox(0.0F, 2.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r16.texOffs(52, 60).addBox(0.0F, 3.95F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r17 = new ModelRenderer(this);
        eye_r17.setPos(-11.1F, -6.3F, 3.3F);
        eyes.addChild(eye_r17);
        setRotationAngle(eye_r17, 0.0F, -1.5708F, 0.7854F);
        eye_r17.texOffs(52, 63).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r18 = new ModelRenderer(this);
        eye_r18.setPos(-11.1F, -7.2F, -3.75F);
        eyes.addChild(eye_r18);
        setRotationAngle(eye_r18, 0.0F, -1.5708F, 0.0F);
        eye_r18.texOffs(42, 63).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r18.texOffs(42, 60).addBox(0.0F, -2.1F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r18.texOffs(42, 57).addBox(0.0F, -4.05F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r18.texOffs(42, 54).addBox(0.0F, -6.05F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r19 = new ModelRenderer(this);
        eye_r19.setPos(-11.1F, -7.1F, 0.85F);
        eyes.addChild(eye_r19);
        setRotationAngle(eye_r19, 0.0F, -1.5708F, 0.0F);
        eye_r19.texOffs(32, 63).addBox(0.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r19.texOffs(32, 54).addBox(0.0F, -6.05F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r19.texOffs(32, 57).addBox(0.0F, -4.05F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);
        eye_r19.texOffs(32, 60).addBox(0.0F, -2.1F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, true);

        eye_r20 = new ModelRenderer(this);
        eye_r20.setPos(-12.5F, -13.75F, 1.9F);
        eyes.addChild(eye_r20);
        setRotationAngle(eye_r20, -1.5708F, 0.0F, 0.0F);
        eye_r20.texOffs(27, 51).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r20.texOffs(17, 51).addBox(-4.75F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r21 = new ModelRenderer(this);
        eye_r21.setPos(-12.5F, -13.75F, -2.6F);
        eyes.addChild(eye_r21);
        setRotationAngle(eye_r21, -1.5708F, 0.0F, 0.0F);
        eye_r21.texOffs(27, 54).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);
        eye_r21.texOffs(17, 54).addBox(-5.0F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r22 = new ModelRenderer(this);
        eye_r22.setPos(-14.75F, -13.5F, 4.0F);
        eyes.addChild(eye_r22);
        setRotationAngle(eye_r22, -2.3562F, 0.0F, 0.0F);
        eye_r22.texOffs(22, 51).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        eye_r23 = new ModelRenderer(this);
        eye_r23.setPos(-14.75F, -13.75F, 0.25F);
        eyes.addChild(eye_r23);
        setRotationAngle(eye_r23, -1.5708F, 0.0F, 0.0F);
        eye_r23.texOffs(22, 51).addBox(-0.5F, 0.0F, -0.4F, 0.5F, 0.5F, 1.0F, 0.0F, false);

        m_body = new ModelRenderer(this);
        m_body.setPos(16.0116F, 31.0133F, -0.0123F);
        Mandom.addChild(m_body);


        m_upperPart = new ModelRenderer(this);
        m_upperPart.setPos(-1.0F, -12.0F, 0.0F);
        m_body.addChild(m_upperPart);


        shoulder = new ModelRenderer(this);
        shoulder.setPos(-14.9937F, -18.5342F, 0.0F);
        m_upperPart.addChild(shoulder);
        setRotationAngle(shoulder, 0.0F, -0.0019F, 0.0F);
        shoulder.texOffs(0, 19).addBox(-8.0063F, -0.9658F, -3.5F, 16.0F, 1.0F, 7.0F, 0.0F, false);

        shoulder_r1 = new ModelRenderer(this);
        shoulder_r1.setPos(-5.5063F, -0.4658F, 0.0F);
        shoulder.addChild(shoulder_r1);
        setRotationAngle(shoulder_r1, 0.0F, 0.0F, -0.1309F);
        shoulder_r1.texOffs(1, 30).addBox(-3.5F, -0.5F, -2.0F, 3.0F, 1.0F, 4.0F, 0.0F, true);

        shoulder_r2 = new ModelRenderer(this);
        shoulder_r2.setPos(-7.5063F, -0.1158F, 0.0F);
        shoulder.addChild(shoulder_r2);
        setRotationAngle(shoulder_r2, 0.0F, 0.0F, -0.2182F);
        shoulder_r2.texOffs(4, 37).addBox(-2.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, true);

        shoulder_r3 = new ModelRenderer(this);
        shoulder_r3.setPos(-9.0063F, 0.1342F, 0.0F);
        shoulder.addChild(shoulder_r3);
        setRotationAngle(shoulder_r3, 0.0F, 0.0F, -0.3054F);
        shoulder_r3.texOffs(2, 42).addBox(-2.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);

        shoulder_r4 = new ModelRenderer(this);
        shoulder_r4.setPos(9.2231F, 0.2734F, 0.0F);
        shoulder.addChild(shoulder_r4);
        setRotationAngle(shoulder_r4, 0.0F, 0.0F, 0.2269F);
        shoulder_r4.texOffs(33, 37).addBox(-1.75F, -0.525F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        shoulder_r5 = new ModelRenderer(this);
        shoulder_r5.setPos(8.7437F, -0.0158F, 0.0F);
        shoulder.addChild(shoulder_r5);
        setRotationAngle(shoulder_r5, 0.0F, 0.0F, 0.0873F);
        shoulder_r5.texOffs(31, 30).addBox(-2.75F, -0.65F, -2.0F, 3.0F, 1.0F, 4.0F, 0.0F, false);

        shoulder_r6 = new ModelRenderer(this);
        shoulder_r6.setPos(9.2231F, 0.0734F, 0.0F);
        shoulder.addChild(shoulder_r6);
        setRotationAngle(shoulder_r6, 0.0F, 0.0F, 0.3142F);
        shoulder_r6.texOffs(31, 42).addBox(-0.25F, -0.475F, -2.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        limbs = new ModelRenderer(this);
        limbs.setPos(-1.0F, -24.0F, 0.0F);
        m_body.addChild(limbs);


        left = new ModelRenderer(this);
        left.setPos(5.2F, 1.9F, 0.5F);
        limbs.addChild(left);


        L1 = new ModelRenderer(this);
        L1.setPos(-18.7F, -8.0F, 0.1F);
        left.addChild(L1);
        L1.texOffs(97, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L1_1 = new ModelRenderer(this);
        L1_1.setPos(0.0F, 3.0F, 0.0F);
        L1.addChild(L1_1);
        setRotationAngle(L1_1, -0.2144F, -0.0003F, -0.1327F);
        L1_1.texOffs(97, 8).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L1_2 = new ModelRenderer(this);
        L1_2.setPos(0.0F, 3.0F, 0.0F);
        L1_1.addChild(L1_2);
        setRotationAngle(L1_2, -0.8347F, 0.0F, 0.0F);
        L1_2.texOffs(97, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L1_3 = new ModelRenderer(this);
        L1_3.setPos(0.0F, 3.0F, 0.0F);
        L1_2.addChild(L1_3);
        setRotationAngle(L1_3, -0.7418F, 0.0F, 0.0F);
        L1_3.texOffs(97, 20).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L1_4 = new ModelRenderer(this);
        L1_4.setPos(0.0F, 2.6F, -0.125F);
        L1_3.addChild(L1_4);
        setRotationAngle(L1_4, -0.798F, 0.2549F, 0.2411F);
        L1_4.texOffs(97, 26).addBox(-0.5F, -0.1F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        paw = new ModelRenderer(this);
        paw.setPos(0.0F, 2.15F, -0.125F);
        L1_4.addChild(paw);
        setRotationAngle(paw, 0.0873F, 0.0F, 0.0F);
        paw.texOffs(96, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, true);

        L2 = new ModelRenderer(this);
        L2.setPos(-17.7F, -8.0F, -1.1F);
        left.addChild(L2);
        setRotationAngle(L2, -1.4835F, 0.0F, 0.0F);
        L2.texOffs(109, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L2_1 = new ModelRenderer(this);
        L2_1.setPos(0.0F, 2.75F, 0.0F);
        L2.addChild(L2_1);
        setRotationAngle(L2_1, -1.024F, 0.6219F, 0.3408F);
        L2_1.texOffs(109, 8).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L2_2 = new ModelRenderer(this);
        L2_2.setPos(0.0F, 3.0F, 0.0F);
        L2_1.addChild(L2_2);
        setRotationAngle(L2_2, 0.6352F, 0.2796F, -0.5045F);
        L2_2.texOffs(109, 14).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L2_3 = new ModelRenderer(this);
        L2_3.setPos(0.0F, 3.0F, 0.0F);
        L2_2.addChild(L2_3);
        setRotationAngle(L2_3, 1.2672F, 0.0036F, -0.001F);
        L2_3.texOffs(109, 20).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L2_4 = new ModelRenderer(this);
        L2_4.setPos(0.0F, 2.875F, -0.125F);
        L2_3.addChild(L2_4);
        setRotationAngle(L2_4, 0.9618F, 0.0F, 0.0F);
        L2_4.texOffs(109, 26).addBox(-0.5F, -0.125F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        paw2 = new ModelRenderer(this);
        paw2.setPos(0.0F, 2.125F, -0.125F);
        L2_4.addChild(paw2);
        setRotationAngle(paw2, 0.3429F, 0.0448F, -0.0805F);
        paw2.texOffs(108, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, true);

        L3 = new ModelRenderer(this);
        L3.setPos(-16.6F, -8.0F, 0.1F);
        left.addChild(L3);
        setRotationAngle(L3, -0.3365F, 0.0998F, -0.6037F);
        L3.texOffs(103, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L3_1 = new ModelRenderer(this);
        L3_1.setPos(0.0F, 3.0F, 0.0F);
        L3.addChild(L3_1);
        setRotationAngle(L3_1, -0.4456F, -0.1974F, 0.0934F);
        L3_1.texOffs(103, 8).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L3_2 = new ModelRenderer(this);
        L3_2.setPos(0.0F, 2.6F, 0.0F);
        L3_1.addChild(L3_2);
        setRotationAngle(L3_2, -0.3639F, -0.3835F, -0.1213F);
        L3_2.texOffs(103, 14).addBox(-0.5F, -0.1F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L3_3 = new ModelRenderer(this);
        L3_3.setPos(0.0F, 3.0F, 0.0F);
        L3_2.addChild(L3_3);
        setRotationAngle(L3_3, -0.159F, -0.2256F, 0.2221F);
        L3_3.texOffs(103, 20).addBox(-0.5F, -0.1F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L3_4 = new ModelRenderer(this);
        L3_4.setPos(0.0F, 3.275F, -0.125F);
        L3_3.addChild(L3_4);
        setRotationAngle(L3_4, -0.305F, 0.0658F, 0.3033F);
        L3_4.texOffs(103, 26).addBox(-0.5F, -0.375F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        paw3 = new ModelRenderer(this);
        paw3.setPos(0.0F, 1.875F, -0.125F);
        L3_4.addChild(paw3);
        setRotationAngle(paw3, -0.7133F, -0.1823F, 0.5564F);
        paw3.texOffs(102, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, true);

        L4 = new ModelRenderer(this);
        L4.setPos(-16.6F, -8.0F, -1.1F);
        left.addChild(L4);
        setRotationAngle(L4, -0.3491F, 0.0F, 0.0F);
        L4.texOffs(115, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L4_1 = new ModelRenderer(this);
        L4_1.setPos(0.0F, 2.75F, 0.0F);
        L4.addChild(L4_1);
        setRotationAngle(L4_1, 0.0F, 0.0F, -0.2618F);
        L4_1.texOffs(115, 8).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L4_2 = new ModelRenderer(this);
        L4_2.setPos(0.0F, 3.0F, 0.0F);
        L4_1.addChild(L4_2);
        setRotationAngle(L4_2, -0.6071F, -0.0511F, -0.2476F);
        L4_2.texOffs(115, 14).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L4_3 = new ModelRenderer(this);
        L4_3.setPos(0.0F, 3.0F, 0.0F);
        L4_2.addChild(L4_3);
        setRotationAngle(L4_3, -0.5257F, -0.0436F, -0.0756F);
        L4_3.texOffs(115, 20).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L4_4 = new ModelRenderer(this);
        L4_4.setPos(0.0F, 2.875F, -0.125F);
        L4_3.addChild(L4_4);
        setRotationAngle(L4_4, -0.6564F, 0.0F, 0.0F);
        L4_4.texOffs(115, 26).addBox(-0.5F, -0.125F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        paw4 = new ModelRenderer(this);
        paw4.setPos(0.0F, 2.125F, -0.125F);
        L4_4.addChild(paw4);
        setRotationAngle(paw4, -0.4784F, 0.0403F, 0.0774F);
        paw4.texOffs(114, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, true);

        L5 = new ModelRenderer(this);
        L5.setPos(-15.5F, -8.0F, -0.5F);
        left.addChild(L5);
        setRotationAngle(L5, 0.3927F, 0.0F, -0.6981F);
        L5.texOffs(121, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L5_1 = new ModelRenderer(this);
        L5_1.setPos(0.0F, 2.75F, 0.0F);
        L5.addChild(L5_1);
        setRotationAngle(L5_1, -1.1295F, -0.1639F, -0.0602F);
        L5_1.texOffs(121, 8).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L5_2 = new ModelRenderer(this);
        L5_2.setPos(0.0F, 3.0F, 0.0F);
        L5_1.addChild(L5_2);
        setRotationAngle(L5_2, -0.4248F, -0.4091F, 0.1563F);
        L5_2.texOffs(121, 14).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L5_3 = new ModelRenderer(this);
        L5_3.setPos(0.0F, 3.0F, 0.0F);
        L5_2.addChild(L5_3);
        setRotationAngle(L5_3, -0.2441F, -0.2842F, 0.2688F);
        L5_3.texOffs(121, 20).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        L5_4 = new ModelRenderer(this);
        L5_4.setPos(0.0F, 2.875F, -0.125F);
        L5_3.addChild(L5_4);
        setRotationAngle(L5_4, -0.3983F, 0.0009F, 0.438F);
        L5_4.texOffs(121, 26).addBox(-0.5F, -0.125F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        paw5 = new ModelRenderer(this);
        paw5.setPos(-0.0516F, 1.9617F, -0.0217F);
        L5_4.addChild(paw5);
        setRotationAngle(paw5, -0.6341F, 0.1582F, 0.2095F);
        paw5.texOffs(120, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, true);

        right = new ModelRenderer(this);
        right.setPos(-19.2F, -6.1F, 0.5F);
        limbs.addChild(right);


        R1 = new ModelRenderer(this);
        R1.setPos(1.7F, 0.0F, 0.1F);
        right.addChild(R1);
        setRotationAngle(R1, 0.8282F, -0.1006F, -0.7065F);
        R1.texOffs(80, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R1_2 = new ModelRenderer(this);
        R1_2.setPos(0.0F, 2.5F, 0.0F);
        R1.addChild(R1_2);
        setRotationAngle(R1_2, -0.829F, 0.0014F, 0.0013F);
        R1_2.texOffs(80, 8).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R1_3 = new ModelRenderer(this);
        R1_3.setPos(0.0F, 3.0F, 0.0F);
        R1_2.addChild(R1_3);
        setRotationAngle(R1_3, -0.6979F, -0.0036F, -0.0043F);
        R1_3.texOffs(80, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R1_4 = new ModelRenderer(this);
        R1_4.setPos(0.0F, 3.0F, 0.0F);
        R1_3.addChild(R1_4);
        setRotationAngle(R1_4, -0.912F, -0.0964F, -0.0887F);
        R1_4.texOffs(80, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R1_5 = new ModelRenderer(this);
        R1_5.setPos(0.0F, 3.125F, -0.125F);
        R1_4.addChild(R1_5);
        setRotationAngle(R1_5, -0.3927F, 0.0F, 0.0F);
        R1_5.texOffs(80, 26).addBox(-0.5F, -0.125F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        paw6 = new ModelRenderer(this);
        paw6.setPos(0.0F, 2.125F, -0.125F);
        R1_5.addChild(paw6);
        setRotationAngle(paw6, -0.5633F, -0.0702F, -0.1106F);
        paw6.texOffs(80, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, false);

        R2 = new ModelRenderer(this);
        R2.setPos(1.7F, -0.25F, -1.1F);
        right.addChild(R2);
        setRotationAngle(R2, -1.3194F, 0.3414F, 0.1405F);
        R2.texOffs(68, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R2_2 = new ModelRenderer(this);
        R2_2.setPos(0.0F, 2.5F, 0.0F);
        R2.addChild(R2_2);
        setRotationAngle(R2_2, -0.4773F, -0.0463F, -0.1222F);
        R2_2.texOffs(68, 8).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R2_3 = new ModelRenderer(this);
        R2_3.setPos(0.0F, 3.0F, 0.0F);
        R2_2.addChild(R2_3);
        setRotationAngle(R2_3, -0.066F, -0.2391F, -0.487F);
        R2_3.texOffs(68, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R2_4 = new ModelRenderer(this);
        R2_4.setPos(0.0F, 3.0F, 0.0F);
        R2_3.addChild(R2_4);
        setRotationAngle(R2_4, 0.5139F, -0.0376F, -0.9838F);
        R2_4.texOffs(68, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R2_5 = new ModelRenderer(this);
        R2_5.setPos(0.0F, 3.0F, 0.0F);
        R2_4.addChild(R2_5);
        setRotationAngle(R2_5, -0.2618F, 0.0F, -1.0036F);
        R2_5.texOffs(68, 26).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        paw7 = new ModelRenderer(this);
        paw7.setPos(0.0F, 2.25F, -0.25F);
        R2_5.addChild(paw7);
        setRotationAngle(paw7, 0.3054F, 0.0F, 0.0F);
        paw7.texOffs(68, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, false);

        R3 = new ModelRenderer(this);
        R3.setPos(0.6F, 0.0F, 0.1F);
        right.addChild(R3);
        setRotationAngle(R3, 0.4891F, -0.6626F, 0.9543F);
        R3.texOffs(74, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R3_2 = new ModelRenderer(this);
        R3_2.setPos(0.0F, 2.5F, 0.0F);
        R3.addChild(R3_2);
        setRotationAngle(R3_2, -0.7418F, 0.0F, 0.0F);
        R3_2.texOffs(74, 8).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R3_3 = new ModelRenderer(this);
        R3_3.setPos(0.0F, 3.0F, 0.0F);
        R3_2.addChild(R3_3);
        setRotationAngle(R3_3, -0.9655F, 0.05F, -0.0715F);
        R3_3.texOffs(74, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R3_4 = new ModelRenderer(this);
        R3_4.setPos(0.0F, 3.0F, 0.0F);
        R3_3.addChild(R3_4);
        setRotationAngle(R3_4, -0.6037F, 0.3286F, -0.219F);
        R3_4.texOffs(74, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R3_5 = new ModelRenderer(this);
        R3_5.setPos(0.0F, 3.025F, -0.125F);
        R3_4.addChild(R3_5);
        setRotationAngle(R3_5, -0.1796F, 0.2572F, -0.0503F);
        R3_5.texOffs(74, 26).addBox(-0.5F, -0.025F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        paw8 = new ModelRenderer(this);
        paw8.setPos(0.0F, 2.225F, -0.125F);
        R3_5.addChild(paw8);
        setRotationAngle(paw8, -0.1304F, -0.0114F, -0.0865F);
        paw8.texOffs(74, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, false);

        R4 = new ModelRenderer(this);
        R4.setPos(0.6F, 0.0F, -1.1F);
        right.addChild(R4);
        setRotationAngle(R4, -0.3491F, 0.0F, 0.5672F);
        R4.texOffs(62, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R4_1 = new ModelRenderer(this);
        R4_1.setPos(0.0F, 2.5F, 0.0F);
        R4.addChild(R4_1);
        setRotationAngle(R4_1, -0.3908F, 0.0007F, 0.3944F);
        R4_1.texOffs(62, 8).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R4_2 = new ModelRenderer(this);
        R4_2.setPos(0.0F, 3.0F, 0.0F);
        R4_1.addChild(R4_2);
        setRotationAngle(R4_2, -0.223F, -0.3478F, -0.3085F);
        R4_2.texOffs(62, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R4_3 = new ModelRenderer(this);
        R4_3.setPos(0.0F, 3.0F, 0.0F);
        R4_2.addChild(R4_3);
        setRotationAngle(R4_3, -1.5809F, 0.3264F, -0.5563F);
        R4_3.texOffs(62, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R4_4 = new ModelRenderer(this);
        R4_4.setPos(0.0F, 3.125F, -0.125F);
        R4_3.addChild(R4_4);
        setRotationAngle(R4_4, 0.2727F, 0.7154F, -0.7445F);
        R4_4.texOffs(62, 26).addBox(-0.5F, -0.125F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        paw9 = new ModelRenderer(this);
        paw9.setPos(0.0F, 2.375F, -0.125F);
        R4_4.addChild(paw9);
        setRotationAngle(paw9, 1.0999F, 0.3286F, -0.219F);
        paw9.texOffs(62, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, false);

        R5 = new ModelRenderer(this);
        R5.setPos(-0.5F, 0.0F, -0.5F);
        right.addChild(R5);
        setRotationAngle(R5, -0.9681F, 0.7398F, 0.2385F);
        R5.texOffs(56, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R5_1 = new ModelRenderer(this);
        R5_1.setPos(0.0F, 2.5F, 0.0F);
        R5.addChild(R5_1);
        setRotationAngle(R5_1, -0.4441F, 0.0452F, -0.244F);
        R5_1.texOffs(56, 8).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R5_2 = new ModelRenderer(this);
        R5_2.setPos(0.0F, 3.0F, 0.0F);
        R5_1.addChild(R5_2);
        setRotationAngle(R5_2, -0.4155F, -0.3582F, -0.69F);
        R5_2.texOffs(56, 14).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R5_3 = new ModelRenderer(this);
        R5_3.setPos(0.0F, 3.0F, 0.0F);
        R5_2.addChild(R5_3);
        setRotationAngle(R5_3, -0.0019F, 0.0F, -1.1345F);
        R5_3.texOffs(56, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        R5_4 = new ModelRenderer(this);
        R5_4.setPos(0.0F, 3.125F, -0.125F);
        R5_3.addChild(R5_4);
        setRotationAngle(R5_4, 0.5977F, 0.0728F, -0.9313F);
        R5_4.texOffs(56, 26).addBox(-0.5F, -0.125F, -0.375F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        paw10 = new ModelRenderer(this);
        paw10.setPos(0.0F, 2.125F, -0.125F);
        R5_4.addChild(paw10);
        setRotationAngle(paw10, -0.2096F, -0.017F, -0.1298F);
        paw10.texOffs(56, 32).addBox(-0.5F, -0.25F, -0.75F, 1.0F, 0.5F, 1.5F, 0.0F, false);
    }
}