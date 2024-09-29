package uk.meow.weever.rotp_mandom.util;
//
//import com.github.standobyte.jojo.util.mc.MCUtil;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.item.ArmorStandEntity;
//import net.minecraft.entity.item.ItemEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.projectile.ProjectileEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.Hand;
//import net.minecraft.world.World;
//import uk.meow.weever.rotp_mandom.capability.PlayerUtilCapProvider;
//import uk.meow.weever.rotp_mandom.config.TPARConfig;
//import uk.meow.weever.rotp_mandom.data.entity.EntityData;
//import uk.meow.weever.rotp_mandom.data.entity.ItemData;
//import uk.meow.weever.rotp_mandom.data.entity.LivingEntityData;
//import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
//import uk.meow.weever.rotp_mandom.data.world.WorldData;
//import uk.meow.weever.rotp_mandom.init.InitItems;
//import uk.meow.weever.rotp_mandom.item.RingoClock;
//import uk.meow.weever.rotp_mandom.network.AddonPackets;
//import uk.meow.weever.rotp_mandom.network.server.TrResetDeathTimePacket;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//
@SuppressWarnings("unused")
@Deprecated
public class OldRewindSystem {
//    public static void saveData(
//            PlayerEntity player,
//            Queue<LivingEntityData> livingEntityData,
//            Queue<ProjectileData> projectileData, Queue<ItemData> itemData,
//            Queue<EntityData> entityData, WorldData worldData
//    ) {
//        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
//            cap.setLivingEntityData(livingEntityData);
//            cap.setProjectileData(projectileData);
//            cap.setItemData(itemData);
//            cap.setEntityData(entityData);
//            cap.setWorldData(worldData);
//            cap.setDataIsEmpty(false);
//        });
//    }
//
//    public static void removeData(PlayerEntity player) {
//        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
//            cap.setLivingEntityData(null);
//            cap.setProjectileData(null);
//            cap.setItemData(null);
//            cap.setEntityData(null);
//            cap.setWorldData(null);
//            cap.setDataIsEmpty(true);
//        });
//    }
//
//    public static void saveAllData(PlayerEntity player, int range) {
//        removeData(player);
//
//        Queue<LivingEntityData> livingEntitiesData = new LinkedList<>();
//        Queue<ProjectileData> projectilesData = new LinkedList<>();
//        Queue<ItemData> itemsData = new LinkedList<>();
//        Queue<EntityData> entitiesData = new LinkedList<>();
//        WorldData worldData = null;
//        if (!player.level.isClientSide()) {
//            if (TPARConfig.getSaveWorld(player.level.isClientSide())) {
//                worldData = WorldData.saveWorldData(player.level);
//            }
//
//            List<Entity> entities = new ArrayList<>();
//            entities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, null));
//            entities.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, null));
//            entities.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, null));
//            entities.add(player);
//
//            entities.forEach(entity -> {
//                if (entity instanceof LivingEntity && TPARConfig.getSaveEntities(player.level.isClientSide())) {
//                    livingEntitiesData.add(LivingEntityData.saveLivingEntityData((LivingEntity) entity));
//                } else if (entity instanceof ProjectileEntity && TPARConfig.getSaveProjectiles(player.level.isClientSide())) {
//                    projectilesData.add(ProjectileData.saveProjectileData((ProjectileEntity) entity));
//                } else if (entity instanceof ItemEntity && TPARConfig.getSaveItems(player.level.isClientSide())) {
//                    itemsData.add(ItemData.saveItemData((ItemEntity) entity));
//                } else if (TPARConfig.getSaveEntities(player.level.isClientSide())) {
//                    entitiesData.add(EntityData.saveEntityData(entity));
//                }
//            });
//            saveData(player, livingEntitiesData, projectilesData, itemsData, entitiesData, worldData);
//        }
//    }
//
//    public static void restoreAllData(PlayerEntity player, int range) {
//        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
//            List<Entity> entities = new ArrayList<>();
//            entities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, null));
//            entities.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, null));
//            entities.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, null));
//            entities.add(player);
//
//            for (Entity entity : entities) {
//                if (entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity)) {
//                    restoreLivingEntityData(cap.getLivingEntityData(), (LivingEntity) entity, entities, player.level);
//                } else if (entity instanceof ProjectileEntity) {
//                    restoreProjectileData(cap.getProjectileData(), (ProjectileEntity) entity, entities, player.level);
//                } else if (entity instanceof ItemEntity) {
//                    restoreItemData(cap.getItemData(), (ItemEntity) entity, entities, player.level);
//                } else {
//                    restoreEntityData(cap.getEntityData(), entity, entities, player.level);
//                }
//            }
//
//            if (cap.getWorldData() != null) {
//                WorldData.rewindWorldData(cap.getWorldData());
//            }
//
//            removeData(player);
//        });
//    }
//
//    private static void restoreLivingEntityData(Queue<LivingEntityData> queue, LivingEntity entity, List<Entity> entities, World level) {
//        queue.forEach(data -> {
//            if (entity == data.getEntity()) {
//                data.getEntity().deathTime = 0;
//                AddonPackets.sendToClientsTrackingAndSelf(new TrResetDeathTimePacket(entity.getId()), entity);
//                if (entities.contains(entity) && !data.isRestored()) {
//                    LivingEntityData.rewindLivingEntityData(data);
//                } else {
//                    LivingEntityData.rewindDeadLivingEntityData(data, level);
//                }
//            }
//        });
//    }
//
//    private static void restoreProjectileData(Queue<ProjectileData> queue, ProjectileEntity entity, List<Entity> entities, World level) {
//        queue.forEach(data -> {
//            if (entity == data.getEntity()) {
//                if (entities.contains(entity) && !data.isRestored()) {
//                    ProjectileData.rewindProjectileData(data);
//                    data.setRestored(true);
//                } else {
//                    entity.remove();
//                }
//            }
//        });
//    }
//
//    private static void restoreItemData(Queue<ItemData> queue, ItemEntity entity, List<Entity> entities, World level) {
//        queue.forEach(data -> {
//            if (entity == data.getEntity()) {
//                if (entities.contains(entity) && !data.isRestored()) {
//                    ItemData.rewindItemData(data);
//                    data.setRestored(true);
//                } else if (!entities.contains(entity)) {
//                    if (!data.isRestored()) {
//                        ItemData.rewindDeadItemData(data, level);
//                    }
//                    entity.remove();
//                }
//            }
//        });
//    }
//
//    private static void restoreEntityData(Queue<EntityData> queue, Entity entity, List<Entity> entities, World level) {
//        queue.forEach(data -> {
//            if (entity == data.getEntity()) {
//                if (entities.contains(entity) && !data.isRestored()) {
//                    EntityData.rewindEntityData(data);
//                } else {
//                    for (int i = 0; i <= 4; i++) {
//                        entity.remove();
//                    }
//                }
//            }
//        });
//    }
//
//    public static boolean getRingoClock(LivingEntity entity, boolean damage, Hand hand) {
//        ItemStack itemStack;
//        if (hand == Hand.MAIN_HAND) {
//            itemStack = entity.getMainHandItem();
//        } else {
//            itemStack = entity.getOffhandItem();
//        }
//        if (itemStack.getItem() == InitItems.RINGO_CLOCK.get()) {
//            if (!itemStack.hasTag()) {
//                CompoundNBT nbt = new CompoundNBT();
//                itemStack.setTag(nbt);
//                nbt.putInt("cracked", 0);
//                return true;
//            } else {
//                int cracked = itemStack.getTag().getInt("cracked");
//                if (cracked < RingoClock.MAX_CAN_BE_CRACKED()) {
//                    if (damage) {
//                        itemStack.getTag().putInt("cracked", cracked + 1);
//                    }
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public static boolean getRingoClock(LivingEntity entity) {
//        return getRingoClock(entity, false);
//    }
//
//    public static boolean getRingoClock(LivingEntity entity, boolean damage) {
//        return getRingoClock(entity, damage, Hand.OFF_HAND);
//    }
////    public static void restoreAllData(PlayerEntity player, int range) {
////        player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> {
////            List<Entity> entities = new ArrayList<>();
////            entities.addAll(MCUtil.entitiesAround(LivingEntity.class, player, range, false, null));
////            entities.addAll(MCUtil.entitiesAround(ProjectileEntity.class, player, range, false, null));
////            entities.addAll(MCUtil.entitiesAround(ItemEntity.class, player, range, false, null));
////            entities.add(player);
////            if (!player.level.isClientSide()) {
////                entities.forEach(entity -> {
////                    if (entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity)) {
////                        cap.getLivingEntityData().forEach(data -> {
////                            if (data != null) {
////                                ((LivingEntity) entity).deathTime = 0;
////                                AddonPackets.sendToClientsTrackingAndSelf(new TrResetDeathTimePacket(entity.getId()), entity);
////                                if (LivingEntityData.ifForRestoringLivingEntityData(entities, data) && !data.isRestored()) {
////                                    LivingEntityData.rewindLivingEntityData(data);
////                                } else if (TPARConfig.getRewindDeadLivingEntities(player.level.isClientSide()) && !data.isRestored()) {
////                                    LivingEntityData.rewindDeadLivingEntityData(data, player.level);
////                                }
////                            }
////                        });
////                    } else if (entity instanceof ProjectileEntity) {
////                        cap.getProjectileData().forEach(data -> {
////                            if (data != null) {
////                                if (ProjectileData.ifForRestoringProjectileEntityData(entities, data) && !data.isRestored()) {
////                                    ProjectileData.rewindProjectileData(data);
////                                } else if (!ProjectileData.ifForRestoringProjectileEntityData(entities, data) && !data.isRestored()) {
////                                    ProjectileData.rewindDeadProjectileEntityData(data, player.level);
////                                } else {
////                                    entity.remove();
////                                }
////                            }
////                        });
////                    } else if (entity instanceof ItemEntity) {
////                        cap.getItemData().forEach(data -> {
////                            if (data != null) {
////                                if (ItemData.ifForRestoringItemData(entities, data) && !data.isRestored()) {
////                                    ItemData.rewindItemData(data);
////                                } else if (!ItemData.ifForRestoringItemData(entities, data) && !data.isRestored()) {
////                                    ItemData.rewindDeadItemData(data, player.level);
////                                } else {
////                                    entity.remove();
////                                }
////                            }
////                        });
////                    } else {
////                        cap.getEntityData().forEach(data -> {
////                            if (data != null) {
////                                if (EntityData.ifForRestoringEntityData(entities, data) && !data.isRestored()) {
////                                    EntityData.rewindEntityData(data);
////                                } else if (!EntityData.ifForRestoringEntityData(entities, data) && !data.isRestored()) {
////                                    EntityData.rewindDeadEntityData(data, player.level);
////                                } else {
////                                    entity.remove();
////                                }
////                            }
////                        });
////                    }
////                });
////                if (cap.getWorldData() != null) {
////                    WorldData.rewindWorldData(cap.getWorldData());
////                }
////                removeData(player);
////            }
////        });
////    }
}
