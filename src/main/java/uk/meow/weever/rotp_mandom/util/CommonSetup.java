package uk.meow.weever.rotp_mandom.util;

import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.init.InitCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    @SubscribeEvent
    public static void onFMLCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(InitCapabilities::registerCapabilities);
    }
}