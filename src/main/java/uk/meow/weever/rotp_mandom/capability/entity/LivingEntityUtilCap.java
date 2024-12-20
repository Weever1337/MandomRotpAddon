package uk.meow.weever.rotp_mandom.capability.entity;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.util.AddonUtil;

import java.util.LinkedList;

public class LivingEntityUtilCap {
    private final LivingEntity livingEntity;
    private LinkedList<LivingEntityData> livingEntityData = new LinkedList<>();
    private int capabilitySeconds = 0;

    public LivingEntityUtilCap(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    public void tick() {
        int maxSize = RewindConfig.getSecond(livingEntity.level.isClientSide());

        if (livingEntity.tickCount % 20 == 0) {
            capabilitySeconds++;
            if (this.livingEntityData.size() > maxSize) {
                this.livingEntityData.removeFirst();
            }
            if (!TimeStopHandler.isTimeStopped(livingEntity.level, livingEntity.blockPosition())) {
                addLivingEntityData(LivingEntityData.saveLivingEntityData(livingEntity), maxSize);
            }
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
        this.capabilitySeconds = oldCap.capabilitySeconds;
    }

    public int getCapabilitySeconds() {
        return capabilitySeconds;
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("capabilitySeconds", capabilitySeconds);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {
        capabilitySeconds = nbt.getInt("capabilitySeconds");
    }
}
