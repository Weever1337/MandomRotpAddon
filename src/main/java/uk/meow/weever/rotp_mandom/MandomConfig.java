package uk.meow.weever.rotp_mandom;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.InMemoryCommentedFormat;
import com.github.standobyte.jojo.client.ClientUtil;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.CommonConfigPacket;
import uk.meow.weever.rotp_mandom.network.server.ResetSyncedCommonConfigPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = MandomAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MandomConfig {

    public static final Client CLIENT;
    static final ForgeConfigSpec commonSpec;
    static final ForgeConfigSpec clientSpec;
    private static final Common COMMON_FROM_FILE;
    private static final Common COMMON_SYNCED_TO_CLIENT;

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

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
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
        public final ForgeConfigSpec.IntValue SaveInChunks;
        public final ForgeConfigSpec.IntValue MaxCastRingoClock;
        public final ForgeConfigSpec.IntValue MaxCastSeconds;
        public final ForgeConfigSpec.BooleanValue SaveInventory;
        public final ForgeConfigSpec.BooleanValue SaveItems;
        public final ForgeConfigSpec.BooleanValue SaveProjectiles;
        public final ForgeConfigSpec.BooleanValue SaveEntities;
        public final ForgeConfigSpec.BooleanValue SaveWorld;
        public final ForgeConfigSpec.BooleanValue RewindDeadLivingEntities;
        public final ForgeConfigSpec.BooleanValue RewindDeadEntities;
        private boolean loaded = false;

        private Common(ForgeConfigSpec.Builder builder) {
            this(builder, null);
        }

        private Common(ForgeConfigSpec.Builder builder, @Nullable String mainPath) {
            if (mainPath != null) {
                builder.push(mainPath);
            }

            builder.push("Global Settings");
            SaveInChunks = builder
                    .translation("rotp_mandom.config.point_chunks")
                    .comment("    Save all in * chunks.",
                            "    Defaults to 10."
                    )
                    .defineInRange("SaveInChunks", 10, 1, Integer.MAX_VALUE);
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
            builder.push("Time Point && Time Rewind Settings");
            MaxCastSeconds = builder
                    .translation("rotp_mandom.config.max_cast_seconds")
                    .comment("    Maximum number of seconds up to remove Time Point data.",
                            "    Defaults to 6 (in canon too).")
                    .defineInRange("MaxCastSeconds", 6, 1, Integer.MAX_VALUE);
            SaveInventory = builder
                    .translation("rotp_mandom.config.save_inventory")
                    .comment("    Save inventory in Time Point ability.",
                            "    Good with Save Items setting",
                            "    WARNING: It's WIP feature and have dupe with Chests",
                            "    Defaults to true.")
                    .define("SaveInventory", true);
            SaveItems = builder
                    .translation("rotp_mandom.config.save_items")
                    .comment("    Save items in Time Point ability.",
                            "    Good with Save Inventory setting",
                            "    Defaults to true.")
                    .define("SaveItems", true);
            SaveProjectiles = builder
                    .translation("rotp_mandom.config.save_projectiles")
                    .comment("    Save projectiles in Time Point ability.",
                            "    Good with Save Items and Save Inventory setting",
                            "    Defaults to true.")
                    .define("SaveProjectiles", true);
            SaveEntities = builder
                    .translation("rotp_mandom.config.save_entities")
                    .comment("    Save entities in Time Point ability.",
                            "    Just good.",
                            "    Defaults to true.")
                    .define("SaveEntities", true);
            SaveWorld = builder
                    .translation("rotp_mandom.config.save_world")
                    .comment("    Save world in Time Point ability.",
                            "    Just good.",
                            "    Defaults to false.")
                    .define("SaveWorld", true);
            RewindDeadLivingEntities = builder
                    .translation("rotp_mandom.config.rewind_dead_living_entities")
                    .comment("    Rewind dead living entities in Time Rewind ability.",
                            "    Defaults to true.")
                    .define("RewindDeadLivingEntities", true);
            RewindDeadEntities = builder
                    .translation("rotp_mandom.config.rewind_dead_entities")
                    .comment("    Rewind dead entities (except living entities, items, projectiles) in Time Rewind ability.",
                            "    Defaults to true.")
                    .define("RewindDeadEntities", true);
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
            private final int SaveInChunks;
            private final int MaxCastRingoClock;
            private final int MaxCastSeconds;
            private final boolean SaveInventory;
            private final boolean SaveItems;
            private final boolean SaveProjectiles;
            private final boolean SaveEntities;
            private final boolean SaveWorld;
            private final boolean RewindDeadLivingEntities;
            private final boolean RewindDeadEntities;

            public SyncedValues(PacketBuffer buf) {
                RewindInChunks = buf.readVarInt();
                SaveInChunks = buf.readVarInt();
//                GlobalShader = buf.readBoolean();
                MaxCastRingoClock = buf.readVarInt();
                MaxCastSeconds = buf.readVarInt();
                SaveInventory = buf.readBoolean();
                SaveItems = buf.readBoolean();
                SaveProjectiles = buf.readBoolean();
                SaveEntities = buf.readBoolean();
                SaveWorld = buf.readBoolean();
                RewindDeadLivingEntities = buf.readBoolean();
                RewindDeadEntities = buf.readBoolean();
            }

            private SyncedValues(Common config) {
                RewindInChunks = config.RewindInChunks.get();
                SaveInChunks = config.SaveInChunks.get();
                MaxCastRingoClock = config.MaxCastRingoClock.get();
                MaxCastSeconds = config.MaxCastSeconds.get();
                SaveInventory = config.SaveInventory.get();
                SaveItems = config.SaveItems.get();
                SaveProjectiles = config.SaveProjectiles.get();
                SaveEntities = config.SaveEntities.get();
                SaveWorld = config.SaveWorld.get();
                RewindDeadLivingEntities = config.RewindDeadLivingEntities.get();
                RewindDeadEntities = config.RewindDeadEntities.get();
            }

            public static void resetConfig() {
                COMMON_SYNCED_TO_CLIENT.RewindInChunks.clearCache();
                COMMON_SYNCED_TO_CLIENT.SaveInChunks.clearCache();
                COMMON_SYNCED_TO_CLIENT.MaxCastRingoClock.clearCache();
                COMMON_SYNCED_TO_CLIENT.MaxCastSeconds.clearCache();
                COMMON_SYNCED_TO_CLIENT.SaveInventory.clearCache();
                COMMON_SYNCED_TO_CLIENT.SaveItems.clearCache();
                COMMON_SYNCED_TO_CLIENT.SaveProjectiles.clearCache();
                COMMON_SYNCED_TO_CLIENT.SaveEntities.clearCache();
                COMMON_SYNCED_TO_CLIENT.SaveWorld.clearCache();
                COMMON_SYNCED_TO_CLIENT.RewindDeadLivingEntities.clearCache();
                COMMON_SYNCED_TO_CLIENT.RewindDeadEntities.clearCache();
            }

            public static void syncWithClient(ServerPlayerEntity player) {
                AddonPackets.sendToClient(new CommonConfigPacket(new SyncedValues(COMMON_FROM_FILE)), player);
            }

            public static void onPlayerLogout(ServerPlayerEntity player) {
                AddonPackets.sendToClient(new ResetSyncedCommonConfigPacket(), player);
            }

            public void writeToBuf(PacketBuffer buf) {
                buf.writeVarInt(RewindInChunks);
                buf.writeVarInt(SaveInChunks);
                buf.writeVarInt(MaxCastRingoClock);
                buf.writeVarInt(MaxCastSeconds);
                buf.writeBoolean(SaveInventory);
                buf.writeBoolean(SaveItems);
                buf.writeBoolean(SaveProjectiles);
                buf.writeBoolean(SaveEntities);
                buf.writeBoolean(SaveWorld);
                buf.writeBoolean(RewindDeadLivingEntities);
                buf.writeBoolean(RewindDeadEntities);
            }

            public void changeConfigValues() {
                COMMON_SYNCED_TO_CLIENT.SaveInChunks.set(SaveInChunks);
                COMMON_SYNCED_TO_CLIENT.RewindInChunks.set(RewindInChunks);
                COMMON_SYNCED_TO_CLIENT.MaxCastRingoClock.set(MaxCastRingoClock);
                COMMON_SYNCED_TO_CLIENT.MaxCastSeconds.set(MaxCastSeconds);
                COMMON_SYNCED_TO_CLIENT.SaveInventory.set(SaveInventory);
                COMMON_SYNCED_TO_CLIENT.SaveItems.set(SaveItems);
                COMMON_SYNCED_TO_CLIENT.SaveProjectiles.set(SaveProjectiles);
                COMMON_SYNCED_TO_CLIENT.SaveEntities.set(SaveEntities);
                COMMON_SYNCED_TO_CLIENT.SaveWorld.set(SaveWorld);
                COMMON_SYNCED_TO_CLIENT.RewindDeadLivingEntities.set(RewindDeadLivingEntities);
                COMMON_SYNCED_TO_CLIENT.RewindDeadEntities.set(RewindDeadEntities);
            }
        }
    }

    public static class Client {
        private Client(ForgeConfigSpec.Builder builder) {
        }
    }
}