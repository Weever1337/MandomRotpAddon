package uk.meow.weever.rotp_mandom.config;

import com.github.standobyte.jojo.init.ModGamerules;

import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.MandomConfig;
import uk.meow.weever.rotp_mandom.init.InitGamerules;

public class TPARConfig {
    public static int getSecond(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).MaxCastSeconds.get();
    }

    @Deprecated
    public static boolean getSaveInventory(boolean clientSide) {
        return getSaveItems(clientSide);
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

    public static boolean getSaveStandStats(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveStandStats.get();
    }

    public static boolean getSaveNonPowerStats(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveNonPowerStats.get();
    }

    public static boolean getRewindDeadLivingEntities(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).RewindDeadLivingEntities.get();
    }

    public static boolean getSaveWorld(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).SaveWorld.get();
    }

    public static boolean getCooldownForRewind(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).CooldownForRewind.get();
    }

    public static uk.meow.weever.rotp_mandom.util.RewindSystem.CooldownSystem getCooldownSystem(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).CooldownSystem.get();
    }

    public static int getCooldownOwnTime(boolean clientSide) {
        return MandomConfig.getCommonConfigInstance(clientSide).CooldownOwnTime.get();
    }

    public static boolean summonStandEnabled(World world) {
        return world.getGameRules().getBoolean(InitGamerules.MANDOM_TOGGLE_SUMMON_STAND_IN_REWIND);
    }
}
