package uk.meow.weever.rotp_mandom.config;

import uk.meow.weever.rotp_mandom.MandomConfig;

public class GlobalConfig {
    public static int getTimeRewindChunks(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).RewindInChunks.get();
    }

    public static int getMaxCastRingoClock(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).MaxCastRingoClock.get();
    }
}
