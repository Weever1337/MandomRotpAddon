package uk.meow.weever.rotp_mandom.util;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import uk.meow.weever.rotp_mandom.config.GlobalConfig;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.data.world.WorldData;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.RWAddClientPlayerData;

import java.util.ArrayList;
import java.util.List;

public class SaveScheduler implements ScheduledTask {
    private final PlayerEntity player;

    public SaveScheduler(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public Runnable execute() {
        return () -> {
            List<LivingEntityData> livingEntityData = new ArrayList<>();
            List<ProjectileData> projectileData = new ArrayList<>();
            List<ItemData> itemData = new ArrayList<>();

            WorldData worldData = WorldData.saveWorldData(player.level);
            int range = GlobalConfig.getTimeRewindChunks(player.level.isClientSide()) * 16;

            List<Entity> entities = new ArrayList<>();
            entities.addAll(MCUtil.entitiesAround(Entity.class, player, range, false, filter ->
                    (filter instanceof LivingEntity || filter instanceof ProjectileEntity || filter instanceof ItemEntity) && !(filter instanceof ArmorStandEntity)));
            entities.add(player);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    if (entity instanceof PlayerEntity) {
                        AddonPackets.sendToClient(new RWAddClientPlayerData(entity.getId(), player.getId()), player); // TODO: do this more... better?
                    }
                    livingEntityData.add(LivingEntityData.saveLivingEntityData((LivingEntity) entity));
                } else if (entity instanceof ItemEntity) {
                    itemData.add(ItemData.saveItemData((ItemEntity) entity));
                } else if (entity instanceof ProjectileEntity) {
                    projectileData.add(ProjectileData.saveProjectileData((ProjectileEntity) entity));
                }
            }

            CapabilityUtil.addLivingEntityData(player, livingEntityData);
            CapabilityUtil.addProjectileEntityData(player, projectileData);
            CapabilityUtil.addItemEntityData(player, itemData);
            CapabilityUtil.addWorldData(player, worldData);
        };
    }
}
