package uk.meow.weever.rotp_mandom.event;

import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.event.loot.PillagerOutpostStructureModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBus {
    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(
                new PillagerOutpostStructureModifier.Serializer().setRegistryName
                        (new ResourceLocation(MandomAddon.MOD_ID, "pillager_op"))
        );
    }
}
