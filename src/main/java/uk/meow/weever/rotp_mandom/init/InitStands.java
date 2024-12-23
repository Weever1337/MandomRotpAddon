package uk.meow.weever.rotp_mandom.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mod.StoryPart;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.action.stand.TimeRewind;
import uk.meow.weever.rotp_mandom.entity.MandomEntity;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), MandomAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), MandomAddon.MOD_ID);

    // ======================================== Mandom ========================================
    public static final RegistryObject<TimeRewind> TIME_REWIND = ACTIONS.register("time_rewind",
            () -> new TimeRewind(new TimeRewind.Builder().autoSummonStand().ignoresPerformerStun()));

    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<MandomEntity>> MANDOM =
            new EntityStandRegistryObject<>("mandom",
                    STANDS,
                    () -> new EntityStandType.Builder<>()
                            .color(0xff6496)
                            .storyPartName(StoryPart.STEEL_BALL_RUN.getName())
                            .rightClickHotbar(TIME_REWIND.get())
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .power(0.0)
                                    .speed(18.0)
                                    .durability(1.0)
                                    .precision(0.0)
                                    .randomWeight(2.0)
                                    .range(2)
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
}