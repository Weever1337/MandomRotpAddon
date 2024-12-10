package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;

import java.util.LinkedList;

public class LivingEntityUtilCap {
    private final LivingEntity livingEntity;
    private LinkedList<LivingEntityData> livingEntityData = new LinkedList<>();

    public LivingEntityUtilCap(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    public void tick() {
        int maxSize = RewindConfig.getSecond(livingEntity.level.isClientSide());

        if (livingEntity.tickCount % 20 == 0) {
            if (this.livingEntityData.size() > maxSize) {
                this.livingEntityData.removeFirst();
            }
            addLivingEntityData(LivingEntityData.saveLivingEntityData(livingEntity), maxSize);
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

    public CompoundNBT toNBT() {
        return new CompoundNBT();
    }

    public void fromNBT(CompoundNBT ignored) {
    }
}
