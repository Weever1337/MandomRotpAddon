package uk.meow.weever.rotp_mandom.client.render;

import com.github.standobyte.jojo.client.render.entity.model.stand.StandEntityModel;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.entity.MandomEntity;

public class MandomRenderer extends StandEntityRenderer<MandomEntity, StandEntityModel<MandomEntity>> {
    public MandomRenderer(EntityRendererManager renderManager) {
        super(renderManager, new MandomModel(), new ResourceLocation(MandomAddon.MOD_ID, "textures/entity/stand/mandom.png"), 0);
    }
}
