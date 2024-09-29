package uk.meow.weever.rotp_mandom.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.TimeStop;
import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.capability.world.TimeStopInstance;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import uk.meow.weever.rotp_mandom.config.GlobalConfig;
import uk.meow.weever.rotp_mandom.config.TPARConfig;
import uk.meow.weever.rotp_mandom.init.InitStands;
import uk.meow.weever.rotp_mandom.util.CapabilityUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import uk.meow.weever.rotp_mandom.util.RewindSystem;

import javax.annotation.Nullable;

public class RestoreDataButInActionMoment extends TimeStop {
    public RestoreDataButInActionMoment(Builder builder) {
        super(builder);
    }

    @Override
    public ActionConditionResult checkConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        return !CapabilityUtil.dataIsEmptyOrNot((PlayerEntity) user) ? ActionConditionResult.POSITIVE : ActionConditionResult.NEGATIVE;
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (!world.isClientSide()) {
            int timeStopTicks = 12;
            int RANGE = GlobalConfig.getTimeRewindChunks(world.isClientSide());
            BlockPos blockPos = user.blockPosition();
            ChunkPos chunkPos = new ChunkPos(blockPos);
            TimeStopInstance instance = new TimeStopInstance(world, timeStopTicks, chunkPos, RANGE, user, this);
            TimeStopHandler.stopTime(world, instance);
            RewindSystem.rewindData((PlayerEntity) user, RANGE * 16);
            RewindSystem.getRingoClock(user, true);
            power.setCooldownTimer(InitStands.TIME_REWIND.get(), TPARConfig.getSecond(world.isClientSide()) * 20);
        }
    }

    @Override
    public @NotNull ResourceLocation getIconTexture(@Nullable IStandPower power) {
        return power.clGetPowerTypeIcon();
    }
}
