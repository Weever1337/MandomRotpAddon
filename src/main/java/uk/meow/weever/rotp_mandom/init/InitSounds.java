package uk.meow.weever.rotp_mandom.init;

import com.github.standobyte.jojo.util.mc.OstSoundList;
import uk.meow.weever.rotp_mandom.MandomAddon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(
            ForgeRegistries.SOUND_EVENTS, MandomAddon.MOD_ID);

    public static final RegistryObject<SoundEvent> USER_SUMMON = SOUNDS.register("summon",
            () -> new SoundEvent(new ResourceLocation(MandomAddon.MOD_ID, "summon"))
    );

    public static final RegistryObject<SoundEvent> STAND_SUMMON = SOUNDS.register("stand_summon",
            () -> new SoundEvent(new ResourceLocation(MandomAddon.MOD_ID, "stand_summon"))
    );

    public static final RegistryObject<SoundEvent> STAND_UNSUMMON = SOUNDS.register("stand_unsummon",
            () -> new SoundEvent(new ResourceLocation(MandomAddon.MOD_ID, "stand_unsummon"))
    );

    public static final RegistryObject<SoundEvent> REWIND = SOUNDS.register("rewind",
            () -> new SoundEvent(new ResourceLocation(MandomAddon.MOD_ID, "rewind"))
    );

    public static final RegistryObject<SoundEvent> VOID = SOUNDS.register("void",
            () -> new SoundEvent(new ResourceLocation(MandomAddon.MOD_ID, "void"))
    );

    static final OstSoundList MANDOM_OST = new OstSoundList(new ResourceLocation(MandomAddon.MOD_ID, "mandom_ost"), SOUNDS);
}
