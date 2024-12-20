package uk.meow.weever.rotp_mandom.data.global;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.meow.weever.rotp_mandom.config.RewindConfig;

public class StandPowerData {
    public final IStandPower stand;
    public final boolean summoned;

    public StandPowerData(IStandPower stand, boolean summoned) {
        this.stand = stand;
        this.summoned = summoned;
    }

    public static void loadData(LivingEntity entity, StandPowerData data) {
        if (entity == null || data == null) return;

        IStandPower.getStandPowerOptional(entity).ifPresent(power -> {
            if (power.getType() == data.stand.getType() && RewindConfig.summonStandEnabled(entity.level.isClientSide())) {
                if (data.summoned && !power.isActive()) {
                    power.toggleSummon();
                } else if (!data.summoned && power.isActive()) {
                    power.toggleSummon();
                }
            }
        });
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("StandType", this.stand.getType().toString());
        nbt.putBoolean("Summoned", this.summoned);
        return nbt;
    }

    public static StandPowerData fromNbt(CompoundNBT nbt, IStandPower stand) {
        boolean summoned = nbt.getBoolean("Summoned");
        return new StandPowerData(stand, summoned);
    }
}
