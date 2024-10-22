package uk.meow.weever.rotp_mandom.entity;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.network.PacketManager;
import com.github.standobyte.jojo.network.packets.fromclient.ClClickActionPacket;
import uk.meow.weever.rotp_mandom.config.TPARConfig;
import uk.meow.weever.rotp_mandom.init.InitStands;
import uk.meow.weever.rotp_mandom.util.CapabilityUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

public class MandomEntity extends StandEntity {
    private static int SEC = -1;
    private static int ticks = 0;
    private final StandRelativeOffset offsetDefault = StandRelativeOffset.withYOffset(0, 0, 0);

    public MandomEntity(StandEntityType<MandomEntity> type, World world) {
        super(type, world);
        unsummonOffset = getDefaultOffsetFromUser().copy();
    }

    public static int getSEC() {
        return SEC;
    }

    public void setSEC(int sec) {
        SEC = sec;
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity user = getUser();
        if (!level.isClientSide()) {
            this.addEffect(new EffectInstance(ModStatusEffects.FULL_INVISIBILITY.get(), 20, 0, false, false));
        }

        if (!(user instanceof PlayerEntity)) {
            return;
        }

        if (CapabilityUtil.dataIsEmptyOrNot((PlayerEntity) user)) {
            SEC = -1;
            ticks = 0;
            return;
        }

        if (this.getCurrentTaskAction() == ModStandsInit.UNSUMMON_STAND_ENTITY.get()) {
            SEC = -1;
            ticks = 0;
            CapabilityUtil.removeRewindData((PlayerEntity) user);
            return;
        }

        if (!TimeStopHandler.isTimeStopped(level, user.blockPosition()) && level.isClientSide()) {
            ticks++;
            if (SEC == -1) {
                SEC = TPARConfig.getSecond(level.isClientSide());
            } else if (ticks % 20 == 0 && SEC != -2) {
                SEC--;

                if (SEC == 0) {
                    SEC = -2;
                    ticks = 0;
                    ClClickActionPacket packet = new ClClickActionPacket(
                            getUserPower().getPowerClassification(), InitStands.REWIND_TIPO.get(), ActionTarget.EMPTY, false
                    );
                    PacketManager.sendToServer(packet);
                }
            }
        }
    }

    public StandRelativeOffset getDefaultOffsetFromUser() {
        return offsetDefault;
    }
}
