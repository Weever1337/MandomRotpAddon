package uk.meow.weever.rotp_mandom.init;

import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.capability.entity.*;
import uk.meow.weever.rotp_mandom.capability.world.WorldUtilCap;
import uk.meow.weever.rotp_mandom.capability.world.WorldUtilCapProvider;

@Mod.EventBusSubscriber(modid = MandomAddon.MOD_ID)
public class InitCapabilities {
    private static final ResourceLocation CLIENT_PLAYER_ENTITY_UTIL = new ResourceLocation(MandomAddon.MOD_ID, "client_player_entity_util");
    private static final ResourceLocation LIVING_ENTITY_UTIL = new ResourceLocation(MandomAddon.MOD_ID, "living_entity_util");
    private static final ResourceLocation PROJECTILE_ENTITY_UTIL = new ResourceLocation(MandomAddon.MOD_ID, "projectile_entity_util");
    private static final ResourceLocation ITEM_ENTITY_UTIL = new ResourceLocation(MandomAddon.MOD_ID, "item_entity_util");
    private static final ResourceLocation WORLD_UTIL = new ResourceLocation(MandomAddon.MOD_ID, "world_util");

    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity) {
            event.addCapability(LIVING_ENTITY_UTIL, new LivingEntityUtilCapProvider((LivingEntity) entity));
            if (entity instanceof PlayerEntity && entity.level.isClientSide()) {
                event.addCapability(CLIENT_PLAYER_ENTITY_UTIL, new ClientPlayerEntityUtilCapProvider((AbstractClientPlayerEntity) entity));
            }
        } else if (entity instanceof ProjectileEntity) {
            event.addCapability(PROJECTILE_ENTITY_UTIL, new ProjectileEntityUtilCapProvider((ProjectileEntity) entity));
        } else if (entity instanceof ItemEntity) {
            event.addCapability(ITEM_ENTITY_UTIL, new ItemEntityUtilCapProvider((ItemEntity) entity));
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        if (!world.isClientSide()) {
            event.addCapability(WORLD_UTIL, new WorldUtilCapProvider(world));
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();

        original.getCapability(LivingEntityUtilCapProvider.CAPABILITY).ifPresent(oldCap -> {
            player.getCapability(LivingEntityUtilCapProvider.CAPABILITY).ifPresent(newCap -> {
                newCap.onClone(oldCap);
            });
        });
    }

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(ClientPlayerEntityUtilCap.class, new ClientPlayerEntityUtilCapStorage(), () -> new ClientPlayerEntityUtilCap(null));
        CapabilityManager.INSTANCE.register(LivingEntityUtilCap.class, new LivingEntityUtilCapStorage(), () -> new LivingEntityUtilCap(null));
        CapabilityManager.INSTANCE.register(ProjectileEntityUtilCap.class, new ProjectileEntityUtilCapStorage(), () -> new ProjectileEntityUtilCap(null));
        CapabilityManager.INSTANCE.register(ItemEntityUtilCap.class, new ItemEntityUtilCapStorage(), () -> new ItemEntityUtilCap(null));
        CapabilityManager.INSTANCE.register(WorldUtilCap.class, JojoModUtil.noStorage(), () -> new WorldUtilCap(null));
    }
}