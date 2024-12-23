package uk.meow.weever.rotp_mandom.data.global;

import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;

public class NonPowerData {
    public final INonStandPower power;
    public final float energy;

    public NonPowerData(INonStandPower power, float energy) {
        this.power = power;
        this.energy = energy;
    }

    public static void loadData(LivingEntity entity, NonPowerData data) {
        if (entity == null || data == null) return;

        INonStandPower.getNonStandPowerOptional(entity).ifPresent(power -> {
            if (power.getType() == data.power.getType()) {
                power.setEnergy(data.energy);
            }
        });
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("PowerType", this.power.getType().toString());
        nbt.putFloat("Energy", this.energy);
        return nbt;
    }

    public static NonPowerData fromNbt(CompoundNBT nbt, INonStandPower power) {
        float energy = nbt.getFloat("Energy");
        return new NonPowerData(power, energy);
    }
}
