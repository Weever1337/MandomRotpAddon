package uk.meow.weever.rotp_mandom.util;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import uk.meow.weever.rotp_mandom.data.entity.*;
import uk.meow.weever.rotp_mandom.data.world.*;
import uk.meow.weever.rotp_mandom.config.*;
import uk.meow.weever.rotp_mandom.init.InitItems;
import uk.meow.weever.rotp_mandom.item.RingoClock;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.TrResetDeathTimePacket;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RewindSystem {
    public static void saveData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            CapabilityUtil.removeRewindData(player);

            Queue<LivingEntityData> livingEntitiesData = new LinkedList<>();
            Queue<ProjectileData> projectilesData = new LinkedList<>();
            Queue<ItemData> itemsData = new LinkedList<>();
            WorldData worldData = null;
            if (TPARConfig.getSaveWorld(player.level.isClientSide())) {
                worldData = WorldData.saveWorldData(player.level);
            }
            if (TPARConfig.getSaveEntities(player.level.isClientSide())) {
                List<LivingEntity> livingEntities = new ArrayList<>();
                livingEntities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, null));
                livingEntities.removeIf(entity -> entity instanceof ArmorStandEntity);
                livingEntities.add(player);
                livingEntities.forEach(entity -> {
                    livingEntitiesData.add(LivingEntityData.saveLivingEntityData((LivingEntity) entity));
                });
            }
            if (TPARConfig.getSaveItems(player.level.isClientSide())) {
                List<ItemEntity> items = new ArrayList<>();
                items.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, null));
                items.forEach(entity -> {
                    itemsData.add(ItemData.saveItemData((ItemEntity) entity));
                });
            }
            if (TPARConfig.getSaveProjectiles(player.level.isClientSide())) {
                List<ProjectileEntity> projectiles = new ArrayList<>();
                projectiles.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, null));
                projectiles.forEach(entity -> {
                    projectilesData.add(ProjectileData.saveProjectileData((ProjectileEntity) entity));
                });
            }
            CapabilityUtil.saveRewindData(player, livingEntitiesData, projectilesData, itemsData, worldData);
        }
    }

    public static void rewindData(PlayerEntity player, int range) {
        if (!player.level.isClientSide()) {
            Queue<LivingEntityData> livingEntityData = CapabilityUtil.getLivingEntityData(player);
            Queue<ProjectileData> projectileData = CapabilityUtil.getProjectileData(player);
            Queue<ItemData> itemData = CapabilityUtil.getItemData(player);
            List<Entity> entities = new ArrayList<>();
            entities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, null));
            entities.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, null));
            entities.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, null));
            entities.addAll(MCUtil.entitiesAround(Entity.class, player, range, false, null));
            entities.add(player);
            entities.forEach(entity -> {
                if (entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity) && livingEntityData != null) {
                    rewindLivingEntityData(livingEntityData, entity, entities);
                } else if (entity instanceof ProjectileEntity && projectileData != null) {
                    rewindProjectileData(projectileData, entity, entities);
                } else if (entity instanceof ItemEntity && itemData != null) {
                    rewindItemData(itemData, entity, entities);
                }
            });
            WorldData worldData = CapabilityUtil.getWorldData(player);
            if (worldData != null) {
                WorldData.rewindWorldData(worldData);
            }
            CapabilityUtil.removeRewindData(player);
        }
    }

    private static void rewindLivingEntityData(Queue<LivingEntityData> livingEntityData, Entity entity, List<Entity> entities) {
        livingEntityData.forEach(data -> {
            if (!data.restored && data.entity == entity) {
                data.entity.deathTime = 0;
                AddonPackets.sendToClientsTrackingAndSelf(new TrResetDeathTimePacket(data.entity.getId()), data.entity);
                if (entities.contains(data.entity)) {
                    LivingEntityData.rewindLivingEntityData(data);
                } else {
                    LivingEntityData.rewindDeadLivingEntityData(data, entity.level);
                }
            }
        });
    }

    private static void rewindProjectileData(Queue<ProjectileData> projectileData, Entity entity, List<Entity> entities) {
        AtomicBoolean foundInWorld = new AtomicBoolean(false);
        projectileData.forEach(data -> {
            if (!data.restored && data.entity == entity) {
                foundInWorld.set(true);
                if (entities.contains(data.entity)) {
                    ProjectileData.rewindProjectileData(data);
                    data.restored = true;
                }
            }
        });
        if (!foundInWorld.get()) {
            if (!inProjectileData(projectileData, (ProjectileEntity) entity)) {
                entity.remove();
            } else {
                projectileData.forEach(data -> {
                    if (!data.restored && entities.contains(entity)) {
                        ProjectileData.rewindDeadProjectileEntityData(data, entity.level);
                        data.restored = true;
                    }
                });
            }
        }
    }

    private static void rewindItemData(Queue<ItemData> itemData, Entity entity, List<Entity> entities) {
        AtomicBoolean foundInWorld = new AtomicBoolean(false);
        itemData.forEach(data -> {
            if (!data.restored && data.entity == entity) {
                foundInWorld.set(true);
                if (inItemData(itemData, (ItemEntity) entity)) {
                    ItemData.rewindItemData(data);
                    data.setRestored(true);
                }
            }
        });
        if (!foundInWorld.get()) {
            if (!inItemData(itemData, (ItemEntity) entity)) {
                entity.remove();
            } else {
                itemData.forEach(data -> {
                    if (!data.restored && entities.contains(entity)) {
                        ItemData.rewindDeadItemData(data, entity.level);
                        data.setRestored(true);
                    }
                });
            }
        }
    }

    public static boolean inItemData(Queue<ItemData> itemData, ItemEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        itemData.forEach(data -> {
            if (data.entity == entity) {
                inData.set(true);
            }
        });
        return inData.get();
    }

    public static boolean inProjectileData(Queue<ProjectileData> projectileData, ProjectileEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        projectileData.forEach(data -> {
            if (data.entity == entity) {
                inData.set(true);
            }
        });
        return inData.get();
    }

    public static boolean getRingoClock(LivingEntity entity, boolean damage, Hand hand) {
        ItemStack itemStack;
        if (hand == Hand.MAIN_HAND) {
            itemStack = entity.getMainHandItem();
        } else {
            itemStack = entity.getOffhandItem();
        }
        if (itemStack.getItem() == InitItems.RINGO_CLOCK.get()) {
            if (!itemStack.hasTag()) {
                CompoundNBT nbt = new CompoundNBT();
                itemStack.setTag(nbt);
                nbt.putInt("cracked", 0);
                return true;
            } else {
                int cracked = itemStack.getTag().getInt("cracked");
                if (cracked < RingoClock.MAX_CAN_BE_CRACKED()) {
                    if (damage) {
                        itemStack.getTag().putInt("cracked", cracked + 1);
                    }
                    return true;
                }
            }
        }    
        return false;
    }

    public static boolean getRingoClock(LivingEntity entity) {
        return getRingoClock(entity, false);
    }

    public static boolean getRingoClock(LivingEntity entity, boolean damage) {
        return getRingoClock(entity, damage, Hand.OFF_HAND);
    }
}
