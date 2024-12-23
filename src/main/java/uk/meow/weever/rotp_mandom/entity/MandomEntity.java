package uk.meow.weever.rotp_mandom.entity;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.init.ModStatusEffects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class MandomEntity extends StandEntity {
    private final StandRelativeOffset offsetDefault = StandRelativeOffset.withYOffset(0, 0, 0);

    public MandomEntity(StandEntityType<MandomEntity> type, World world) {
        super(type, world);
        unsummonOffset = getDefaultOffsetFromUser().copy();
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide()) {
            this.addEffect(new EffectInstance(ModStatusEffects.FULL_INVISIBILITY.get(), 4, 2, false, false));
        }
    }

    public StandRelativeOffset getDefaultOffsetFromUser() {
        return offsetDefault;
    }
}
