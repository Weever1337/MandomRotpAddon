package uk.meow.weever.rotp_mandom.data.global;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ExperienceData {
    public final int level;
    public final int points;

    public ExperienceData(int level, int points) {
        this.level = level;
        this.points = points;
    }

    public static int getExperiencePoints(PlayerEntity player) {
        float f = (float) player.getXpNeededForNextLevel();
        return Math.round(player.experienceProgress * f);
    }

    public static void loadExperienceData(PlayerEntity player, ExperienceData data) {
        if (player == null || data == null) return;

        player.experienceLevel = data.level;
        ((ServerPlayerEntity) player).setExperiencePoints(data.points);
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Level", this.level);
        nbt.putInt("Points", this.points);
        return nbt;
    }

    public static ExperienceData fromNbt(CompoundNBT nbt) {
        int level = nbt.getInt("Level");
        int points = nbt.getInt("Points");
        return new ExperienceData(level, points);
    }
}