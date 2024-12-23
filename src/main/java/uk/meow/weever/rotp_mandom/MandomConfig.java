package uk.meow.weever.rotp_mandom;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.InMemoryCommentedFormat;
import com.github.standobyte.jojo.client.ClientUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.CommonConfigPacket;
import uk.meow.weever.rotp_mandom.network.server.ResetSyncedCommonConfigPacket;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = MandomAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MandomConfig {
    static final ForgeConfigSpec commonSpec;
    private static final Common COMMON_FROM_FILE;
    private static final Common COMMON_SYNCED_TO_CLIENT;
    public static final Client CLIENT;
    static final ForgeConfigSpec clientSpec;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON_FROM_FILE = specPair.getLeft();

        final Pair<Common, ForgeConfigSpec> syncedSpecPair = new ForgeConfigSpec.Builder().configure(builder -> new Common(builder, "synced"));
        CommentedConfig config = CommentedConfig.of(InMemoryCommentedFormat.defaultInstance());
        ForgeConfigSpec syncedSpec = syncedSpecPair.getRight();
        syncedSpec.correct(config);
        syncedSpec.setConfig(config);
        COMMON_SYNCED_TO_CLIENT = syncedSpecPair.getLeft();
    }

    @SuppressWarnings("unused")
    private static boolean isElementNonNegativeFloat(Object num, boolean moreThanZero) {
        if (num instanceof Double) {
            Double numDouble = (Double) num;
            return (numDouble > 0 || !moreThanZero && numDouble == 0) && Float.isFinite(numDouble.floatValue());
        }
        return false;
    }

    public static Common getCommonConfigInstance(boolean isClientSide) {
        return isClientSide && !ClientUtil.isLocalServer() ? COMMON_SYNCED_TO_CLIENT : COMMON_FROM_FILE;
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (MandomAddon.MOD_ID.equals(config.getModId()) && config.getType() == ModConfig.Type.COMMON) {
            COMMON_FROM_FILE.onLoadOrReload();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfig.Reloading event) {
        ModConfig config = event.getConfig();
        if (MandomAddon.MOD_ID.equals(config.getModId()) && config.getType() == ModConfig.Type.COMMON) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.getPlayerList().getPlayers().forEach(Common.SyncedValues::syncWithClient);
            }
        }
    }

    public static class Common {
        public final ForgeConfigSpec.IntValue RewindInChunks;
        public final ForgeConfigSpec.IntValue MaxCastRingoClock;
        public final ForgeConfigSpec.BooleanValue RequireRingoClockToRewind;
        public final ForgeConfigSpec.BooleanValue SaveStandStats;
        public final ForgeConfigSpec.BooleanValue SaveNonPowerStats;
        public final ForgeConfigSpec.BooleanValue SummonStandEnabled;
        private boolean loaded = false;

        private Common(ForgeConfigSpec.Builder builder) {
            this(builder, null);
        }

        private Common(ForgeConfigSpec.Builder builder, @Nullable String mainPath) {
            if (mainPath != null) {
                builder.push(mainPath);
            }

            builder.push("Global Settings");
            RewindInChunks = builder
                    .translation("rotp_mandom.config.rewind_chunks")
                    .comment("    Rewind all in * chunks.",
                            "    Defaults to 10."
                    )
                    .defineInRange("RewindInChunks", 10, 1, Integer.MAX_VALUE);
            MaxCastRingoClock = builder
                    .translation("rotp_mandom.config.max_cast_ringo_clock")
                    .comment("    Maximum number of uses up to repair Ringo Clock.",
                            "    Defaults to 255.")
                    .defineInRange("MaxCastRingoClock", 255, 1, Integer.MAX_VALUE);
            builder.pop();
            builder.push("Time Rewind Settings");
            RequireRingoClockToRewind = builder
                    .translation("rotp_mandom.config.require_ringo_clock_to_rewind")
                    .comment("    Require Ringo Clock to use Time Rewind ability.",
                            "    Defaults to true.")
                    .define("RequireRingoClockToRewind", true);
            SaveStandStats = builder
                    .translation("rotp_mandom.config.save_stand_stats")
                    .comment("    Save stand stats.",
                            "    Defaults to true.")
                    .define("SaveStandStats", true);
            SaveNonPowerStats = builder
                    .translation("rotp_mandom.config.save_non_power_stats")
                    .comment("    Save non power stats.",
                            "    Defaults to true.")
                    .define("SaveNonPowerStats", true);
            SummonStandEnabled = builder
                    .translation("rotp_mandom.config.summon_stand_enabled")
                    .comment("    Allow summoning stand in rewind.",
                            "    Defaults to false.")
                    .define("SummonStandEnabled", false);
            builder.pop();

            if (mainPath != null) {
                builder.pop();
            }
        }

        public boolean isConfigLoaded() {
            return loaded;
        }

        private void onLoadOrReload() {
            loaded = true;
        }

        public static class SyncedValues {
            private final int RewindInChunks;
            private final int MaxCastRingoClock;
            private final boolean RequireRingoClockToRewind;
            private final boolean SaveStandStats;
            private final boolean SaveNonPowerStats;
            private final boolean SummonStandEnabled;

            public SyncedValues(PacketBuffer buf) {
                RewindInChunks = buf.readVarInt();
                MaxCastRingoClock = buf.readVarInt();
                RequireRingoClockToRewind = buf.readBoolean();
                SaveStandStats = buf.readBoolean();
                SaveNonPowerStats = buf.readBoolean();
                SummonStandEnabled = buf.readBoolean();
            }

            private SyncedValues(Common config) {
                RewindInChunks = config.RewindInChunks.get();
                MaxCastRingoClock = config.MaxCastRingoClock.get();
                RequireRingoClockToRewind = config.RequireRingoClockToRewind.get();
                SaveStandStats = config.SaveStandStats.get();
                SaveNonPowerStats = config.SaveNonPowerStats.get();
                SummonStandEnabled = config.SummonStandEnabled.get();
            }

            public static void resetConfig() {
                COMMON_SYNCED_TO_CLIENT.RewindInChunks.clearCache();
                COMMON_SYNCED_TO_CLIENT.MaxCastRingoClock.clearCache();
                COMMON_SYNCED_TO_CLIENT.RequireRingoClockToRewind.clearCache();
                COMMON_SYNCED_TO_CLIENT.SaveStandStats.clearCache();
                COMMON_SYNCED_TO_CLIENT.SaveNonPowerStats.clearCache();
                COMMON_SYNCED_TO_CLIENT.SummonStandEnabled.clearCache();
            }

            public static void syncWithClient(ServerPlayerEntity player) {
                AddonPackets.sendToClient(new CommonConfigPacket(new SyncedValues(COMMON_FROM_FILE)), player);
            }

            public static void onPlayerLogout(ServerPlayerEntity player) {
                AddonPackets.sendToClient(new ResetSyncedCommonConfigPacket(), player);
            }

            public void writeToBuf(PacketBuffer buf) {
                buf.writeVarInt(RewindInChunks);
                buf.writeVarInt(MaxCastRingoClock);
                buf.writeBoolean(RequireRingoClockToRewind);
                buf.writeBoolean(SaveStandStats);
                buf.writeBoolean(SaveNonPowerStats);
                buf.writeBoolean(SummonStandEnabled);
            }

            public void changeConfigValues() {
                COMMON_SYNCED_TO_CLIENT.RewindInChunks.set(RewindInChunks);
                COMMON_SYNCED_TO_CLIENT.MaxCastRingoClock.set(MaxCastRingoClock);
                COMMON_SYNCED_TO_CLIENT.RequireRingoClockToRewind.set(RequireRingoClockToRewind);
                COMMON_SYNCED_TO_CLIENT.SaveStandStats.set(SaveStandStats);
                COMMON_SYNCED_TO_CLIENT.SaveNonPowerStats.set(SaveNonPowerStats);
                COMMON_SYNCED_TO_CLIENT.SummonStandEnabled.set(SummonStandEnabled);
            }
        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue isShaderEnabled;

        private Client(ForgeConfigSpec.Builder builder) {
            builder.push("Config");
            isShaderEnabled = builder.translation("rotp_mandom.config.client.isShaderEnabled")
                    .comment("  Determines if rewind shaders are enabled",
                            "  Default is to true.")
                    .define("isShaderEnabled", true);
            builder.pop();
        }
    }
}