package uk.meow.weever.rotp_mandom.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class TaskScheduler {
    private static final List<ScheduledTask> tasks = new ArrayList<>();

    public static void scheduleTask(ScheduledTask task) {
        tasks.add(task);
    }

    public static void tick(MinecraftServer server) {
        List<ScheduledTask> completedTasks = new ArrayList<>();
        for (ScheduledTask task : tasks) {
            server.execute(task.execute());
            completedTasks.add(task);
        }
        tasks.removeAll(completedTasks);
    }
}

interface ScheduledTask {
    Runnable execute();
}


