package uk.meow.weever.rotp_mandom.action.stand;

import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.client.standskin.StandSkinsManager;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import uk.meow.weever.rotp_mandom.config.GlobalConfig;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.init.InitSounds;
import uk.meow.weever.rotp_mandom.init.InitStands;
import uk.meow.weever.rotp_mandom.util.RewindSystem;

import javax.annotation.Nullable;

public class TimeRewind extends StandEntityAction {
    private final LazySupplier<ResourceLocation> rewindTex =
            new LazySupplier<>(() -> makeIconVariant(this, "_"));

    private ResourceLocation getActionTexture(IStandPower stand) {
        return StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
                .getStandSkin(stand.getStandInstance().get()), rewindTex.get());
    }

    public TimeRewind(Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        LivingEntity livingEntity = userPower.getUser();
        if (!world.isClientSide()) {
            world.playSound(null, livingEntity.blockPosition(), InitSounds.REWIND_START.get(), SoundCategory.PLAYERS, 1, 1);
            int RANGE = GlobalConfig.getTimeRewindChunks(world.isClientSide()) * 16;
            RewindSystem.rewindData(livingEntity, RANGE);
            RewindSystem.getRingoClock(livingEntity, true);
            if (RewindConfig.getCooldownForRewind(world.isClientSide())) {
                if (RewindConfig.getCooldownSystem(world.isClientSide()) == RewindSystem.CooldownSystem.OWN) {
                    userPower.setCooldownTimer(InitStands.TIME_REWIND.get(), RewindConfig.getCooldownOwnTime(world.isClientSide()) * 20);
                } else {
                    userPower.setCooldownTimer(InitStands.TIME_REWIND.get(), RewindConfig.getSecond(world.isClientSide()) * 20);
                }
            }
        }
    }

    @Override
    public boolean canUserSeeInStoppedTime(LivingEntity user, IStandPower power) {
        return false;
    }

    @Override
    public @NotNull ResourceLocation getIconTexture(@Nullable IStandPower power) {
        return getActionTexture(power);
    }

    @Override
    public StandAction[] getExtraUnlockable() {
        return new StandAction[]{
                InitStands.REWIND_TIPO.get()
        };
    }
}