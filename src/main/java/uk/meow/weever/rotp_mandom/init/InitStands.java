package uk.meow.weever.rotp_mandom.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.stats.TimeStopperStandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mod.StoryPart;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.action.stand.RestoreDataButInActionMoment;
import uk.meow.weever.rotp_mandom.action.stand.TimeRewind;
import uk.meow.weever.rotp_mandom.entity.MandomEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), MandomAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), MandomAddon.MOD_ID);

    // ======================================== Mandom ========================================
    public static final RegistryObject<TimeRewind> TIME_REWIND = ACTIONS.register("time_rewind",
            () -> new TimeRewind(new TimeRewind.Builder()));
    public static final EntityStandRegistryObject<EntityStandType<TimeStopperStandStats>, StandEntityType<MandomEntity>> STAND_MANDOM =
            new EntityStandRegistryObject<>("mandom",
                    STANDS,
                    () -> new EntityStandType.Builder<TimeStopperStandStats>()
                            .color(0xff6994)
                            .storyPartName(StoryPart.STEEL_BALL_RUN.getName())
                            .rightClickHotbar(TIME_REWIND.get())
                            .defaultMMB(TIME_REWIND.get())
                            .defaultStats(TimeStopperStandStats.class, new TimeStopperStandStats.Builder()
                                    .power(0.0)
                                    .speed(16.0)
                                    .durability(4.0)
                                    .precision(0.0)
                                    .randomWeight(2.0)
                                    .range(1, 1)
                                    .timeStopMaxTicks(1, 1)
                                    .timeStopCooldownPerTick(20)
                                    .timeStopDecayPerDay(0)
                                    .timeStopLearningPerTick(0)
                                    .build("Mandom")
                            )
                            .disableManualControl().disableStandLeap()
                            .addSummonShout(InitSounds.USER_SUMMON)
                            .addOst(InitSounds.MANDOM_OST)
                            .build(),
                    InitEntities.ENTITIES,
                    () -> new StandEntityType<>(MandomEntity::new, 0, 0)
                            .summonSound(InitSounds.STAND_SUMMON)
                            .unsummonSound(InitSounds.STAND_UNSUMMON))
                    .withDefaultStandAttributes();

    public static final RegistryObject<RestoreDataButInActionMoment> REWIND_TIPO = ACTIONS.register("restore_data",
            () -> new RestoreDataButInActionMoment(new RestoreDataButInActionMoment.Builder()
                    .ignoresPerformerStun()
                    .shaderEffect(new ResourceLocation(MandomAddon.MOD_ID, "shaders/post/mandom.json"), true)
                    .shaderEffect(new ResourceLocation(MandomAddon.MOD_ID, "shaders/post/mandom_old.json"), false)
                    .autoSummonStand()
            ));
}