package uk.meow.weever.rotp_mandom.init;

import com.github.standobyte.jojo.util.mc.reflection.CommonReflection;

import net.minecraft.world.GameRules;

public class InitGamerules {
    public static void load() {}

    public static final GameRules.RuleKey<GameRules.BooleanValue> MANDOM_TOGGLE_SUMMON_STAND_IN_REWIND = GameRules.register(
            "mandomToggleSummonStandInRewind", GameRules.Category.PLAYER, createBoolean(false));
    
    private static GameRules.RuleType<GameRules.BooleanValue> createBoolean(boolean defaultValue) {
        return CommonReflection.createBooleanGameRule(defaultValue);
    }
    
}
