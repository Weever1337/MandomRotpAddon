package uk.meow.weever.rotp_mandom;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.meow.weever.rotp_mandom.init.*;
import uk.meow.weever.rotp_mandom.network.AddonPackets;

@Mod(MandomAddon.MOD_ID)
public class MandomAddon {
    public static final String MOD_ID = "rotp_mandom";
    public static final Logger LOGGER = LogManager.getLogger();

    public MandomAddon() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MandomConfig.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MandomConfig.commonSpec);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        InitEntities.ENTITIES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::onFMLCommonSetup);
    }

    private void onFMLCommonSetup(FMLCommonSetupEvent event) {
        InitCapabilities.registerCapabilities();
        AddonPackets.init();
    }
}