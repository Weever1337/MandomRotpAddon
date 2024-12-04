package uk.meow.weever.rotp_mandom.config;

import uk.meow.weever.rotp_mandom.MandomConfig;
import uk.meow.weever.rotp_mandom.util.RewindSystem;

public class RewindConfig {
    public static int getSecond(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).MaxCastSeconds.get();
    }

    public static boolean getSaveStandStats(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveStandStats.get();
    }

    public static boolean getSaveNonPowerStats(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveNonPowerStats.get();
    }

    public static boolean getCooldownForRewind(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).CooldownForRewind.get();
    }

    public static RewindSystem.CooldownSystem getCooldownSystem(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).CooldownSystem.get();
    }

    public static int getCooldownOwnTime(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).CooldownOwnTime.get();
    }

    public static boolean summonStandEnabled(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SummonStandEnabled.get();
    }
}
