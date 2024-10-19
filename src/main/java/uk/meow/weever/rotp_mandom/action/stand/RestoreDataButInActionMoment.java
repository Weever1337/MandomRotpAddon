package uk.meow.weever.rotp_mandom.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.config.GlobalConfig;
import uk.meow.weever.rotp_mandom.config.TPARConfig;
import uk.meow.weever.rotp_mandom.entity.MandomEntity;
import uk.meow.weever.rotp_mandom.init.InitSounds;
import uk.meow.weever.rotp_mandom.init.InitStands;
import uk.meow.weever.rotp_mandom.util.CapabilityUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import uk.meow.weever.rotp_mandom.util.RewindSystem;

import javax.annotation.Nullable;

public class RestoreDataButInActionMoment extends StandEntityAction {
    private static final ResourceLocation MANDOM_SHADER = new ResourceLocation(MandomAddon.MOD_ID, "shaders/post/mandom.json");
    public RestoreDataButInActionMoment(Builder builder) {
        super(builder);
    }

    // private ResourceLocation getShader(IStandPower power) {
    //     ResourceLocation texture = StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
    //             .getStandSkin(power.getStandInstance().get()), MANDOM_SHADER);
    //     return texture;
    // }

    @Override
    public ActionConditionResult checkConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        return !CapabilityUtil.dataIsEmptyOrNot((PlayerEntity) user) ? ActionConditionResult.POSITIVE : ActionConditionResult.NEGATIVE;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower power, StandEntityTask task) {
        if (!world.isClientSide()) {
            LivingEntity user = power.getUser();
            world.playSound(null,user.blockPosition(), InitSounds.REWIND.get(), SoundCategory.PLAYERS,1,1);
            // if (user instanceof ServerPlayerEntity) {
            //     AddonPackets.sendToClient(new RWSetMandomShader(user.getId(), MANDOM_SHADER), (ServerPlayerEntity) user);
            // }
            int RANGE = GlobalConfig.getTimeRewindChunks(world.isClientSide());
            RewindSystem.rewindData((PlayerEntity) user, RANGE * 16);
            RewindSystem.getRingoClock(user, true);
            if (TPARConfig.getCooldownForRewind(world.isClientSide())) {
                if (TPARConfig.getCooldownSystem(world.isClientSide()) == uk.meow.weever.rotp_mandom.util.RewindSystem.CooldownSystem.OWN) {
                    power.setCooldownTimer(InitStands.TIME_REWIND.get(), TPARConfig.getCooldownOwnTime(world.isClientSide()) * 20);
                } else {
                    power.setCooldownTimer(InitStands.TIME_REWIND.get(), TPARConfig.getSecond(world.isClientSide()) * 20);
                }
            }
            // if (!user.hasEffect(ModStatusEffects.RESOLVE.get()) &&user instanceof ServerPlayerEntity) {
            //     AddonPackets.sendToClient(new RWRemoveMandomShader(user.getId()), (ServerPlayerEntity) user);
            // }
            MandomEntity mandom = (MandomEntity) standEntity;
            mandom.setSEC(-2);
        }
    }

    @Override
    public @NotNull ResourceLocation getIconTexture(@Nullable IStandPower power) {
        return power.clGetPowerTypeIcon();
    }
}
