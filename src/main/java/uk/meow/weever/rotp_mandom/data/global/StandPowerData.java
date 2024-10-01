package uk.meow.weever.rotp_mandom.data.global;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;

public class StandPowerData {
    public final IStandPower stand;
    public final boolean summoned;

    public StandPowerData(IStandPower stand, boolean summoned) {
        this.stand = stand;
        this.summoned = summoned;
    }
}
