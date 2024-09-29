package uk.meow.weever.rotp_mandom.client.render;

import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import uk.meow.weever.rotp_mandom.entity.MandomEntity;

public class MandomModel extends HumanoidStandModel<MandomEntity> {
    public MandomModel() {
        super();
        addHumanoidBaseBoxes(null);
        texWidth = 128;
        texHeight = 128;
    }


}