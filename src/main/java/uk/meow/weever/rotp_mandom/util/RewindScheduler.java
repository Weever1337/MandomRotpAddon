package uk.meow.weever.rotp_mandom.util;

import net.minecraft.entity.player.PlayerEntity;

public class RewindScheduler implements ScheduledTask {
    private final PlayerEntity player;

    public RewindScheduler(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public Runnable execute() {
        return () -> RewindSystem.rewindData(player, 0);
    }
}
