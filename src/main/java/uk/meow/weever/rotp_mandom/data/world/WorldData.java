package uk.meow.weever.rotp_mandom.data.world;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class WorldData {
    private final World level;
    private final long time;

    public WorldData(World level, long time) {
        this.level = level;
        this.time = time;
    }

    public static void rewindWorldData(WorldData worldData) {
        if (worldData == null) return;
        ((ServerWorld) worldData.level).setDayTime(worldData.time);
    }

    public static WorldData saveWorldData(World level) {
        return new WorldData(
                level,
                level.getDayTime()
        );
    }
}
