package uk.meow.weever.rotp_mandom.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.meow.weever.rotp_mandom.MandomConfig;

public class RewindConfig {
    public static int getSecond() {
        return 6;
    }

    public static boolean getSaveStandStats(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveStandStats.get();
    }

    public static boolean getSaveNonPowerStats(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveNonPowerStats.get();
    }

    public static boolean summonStandEnabled(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SummonStandEnabled.get();
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isShaderEnabled() {
        return MandomConfig.CLIENT.isShaderEnabled.get();
    }
}
