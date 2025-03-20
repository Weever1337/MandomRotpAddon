package uk.meow.weever.rotp_mandom.capability.entity;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.init.InitStands;

import java.util.LinkedList;
import java.util.Optional;

public class LivingEntityUtilCap {
    private final LivingEntity livingEntity;
    private LinkedList<LivingEntityData> livingEntityData = new LinkedList<>();
//    private int canUseRewind = -1;

    public LivingEntityUtilCap(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    public void tick() {
        int maxSize = RewindConfig.getSecond();

        if (livingEntity.tickCount % 20 == 0) {
            if (!TimeStopHandler.isTimeStopped(livingEntity.level, livingEntity.blockPosition())) {
                addLivingEntityData(LivingEntityData.saveLivingEntityData(livingEntity), maxSize);
            }
//            Optional<IStandPower> power = IStandPower.getStandPowerOptional(livingEntity).resolve();
//            if (power.isPresent() && power.get().getType() == InitStands.MANDOM.getStandType()) {
//                canUseRewind++;
//            } else {
//                canUseRewind = -1;
//            }
        }
    }

    public LinkedList<LivingEntityData> getLivingEntityData() {
        return livingEntityData;
    }

    public void addLivingEntityData(LivingEntityData livingEntityData, int maxSize) {
        if (this.livingEntityData.size() > maxSize) {
            this.livingEntityData.removeFirst();
        }
        this.livingEntityData.addLast(livingEntityData);
    }

    public void onClone(LivingEntityUtilCap oldCap){
        this.livingEntityData = oldCap.livingEntityData;
    }

//    public int getCanUseRewind() {
//        return canUseRewind;
//    }
//
//    public void setCanUseRewind(int canUseRewind) {
//        this.canUseRewind = canUseRewind;
//    }
}
