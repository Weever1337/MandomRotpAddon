package uk.meow.weever.rotp_mandom.data.global;

import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;

public class NonPowerData {
    public final INonStandPower power;
    public final float energy;

    public NonPowerData(INonStandPower power, float energy) {
        this.power = power;
        this.energy = energy;
    }
}
