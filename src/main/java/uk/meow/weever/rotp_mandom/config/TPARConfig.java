package uk.meow.weever.rotp_mandom.config;

import uk.meow.weever.rotp_mandom.MandomConfig;

public class TPARConfig {
    public static int getSecond(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).MaxCastSeconds.get();
    }

    public static boolean getSaveInventory(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveInventory.get();
    }

    public static boolean getSaveItems(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveItems.get();
    }

    public static boolean getSaveProjectiles(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveProjectiles.get();
    }

    public static boolean getSaveEntities(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveEntities.get();
    }

    public static boolean getRewindDeadLivingEntities(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).RewindDeadLivingEntities.get();
    }

    public static boolean getRewindDeadEntities(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).RewindDeadEntities.get();
    }

    public static boolean getSaveWorld(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveWorld.get();
    }
}
