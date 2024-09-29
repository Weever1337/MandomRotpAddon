package uk.meow.weever.rotp_mandom.init;

import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.capability.PlayerUtilCap;
import uk.meow.weever.rotp_mandom.capability.PlayerUtilCapProvider;
import uk.meow.weever.rotp_mandom.capability.PlayerUtilCapStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID)
public class InitCapabilities {
    private static final ResourceLocation PLAYER_UTIL = new ResourceLocation(MandomAddon.MOD_ID, "player_util");

    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(PLAYER_UTIL, new PlayerUtilCapProvider((PlayerEntity) entity));
        }
    }

    /*@SubscribeEvent
    public static void onEntityTracking(PlayerEvent.StartTracking event) {
        Entity entityTracked = event.getTarget();
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        if (entityTracked instanceof PlayerEntity) {
            player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
                cap.onTracking(player);
            });
        }
    }*/

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(PlayerUtilCap.class, new PlayerUtilCapStorage(), () -> new PlayerUtilCap(null));
    }
}