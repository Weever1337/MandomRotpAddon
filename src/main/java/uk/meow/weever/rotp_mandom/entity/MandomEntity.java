package uk.meow.weever.rotp_mandom.entity;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.util.CapabilityUtil;

public class MandomEntity extends StandEntity {
    private final StandRelativeOffset offsetDefault = StandRelativeOffset.withYOffset(0, 0, 0);

    public MandomEntity(StandEntityType<MandomEntity> type, World world) {
        super(type, world);
        unsummonOffset = getDefaultOffsetFromUser().copy();
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity user = getUser();
        if (!level.isClientSide()) {
            this.addEffect(new EffectInstance(ModStatusEffects.FULL_INVISIBILITY.get(), 4, 2, false, false));
        }

        if (!(user instanceof PlayerEntity)) {
            return;
        }

        if (this.getCurrentTaskAction() == ModStandsInit.UNSUMMON_STAND_ENTITY.get()) {
            CapabilityUtil.removeRewindData((PlayerEntity) user);
        }
    }

    public StandRelativeOffset getDefaultOffsetFromUser() {
        return offsetDefault;
    }
}
