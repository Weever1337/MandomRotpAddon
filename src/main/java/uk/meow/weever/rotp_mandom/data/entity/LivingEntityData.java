package uk.meow.weever.rotp_mandom.data.entity;

import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.config.TPARConfig;
import uk.meow.weever.rotp_mandom.data.global.*;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.TrResetDeathTimePacket;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LivingEntityData implements IEntityData<LivingEntityData, LivingEntity> {
    public LivingEntity entity;
    private final Vector3d position;
    private final float health;
    private final LookData lookData;
    private final int fireTicks;
    private final Set<String> tags;
    private final Map<Integer, ItemStack> inventory;
    private final Map<Integer, ItemStack> enderChest;
    private final Map<Integer, ItemStack> armor;
    private final Map<Integer, ItemStack> offhand;
    private final int selectedSlot;
    private float absorptionAmount;
    private float fallDistance;
    private ExperienceData experienceData;
    private final StandPowerData standPowerData;
    private final NonPowerData nonPowerData;
    private final LivingEntity target;
    public boolean restored;

    public LivingEntityData(LivingEntity entity, Vector3d position, float health, LookData lookData, int fireTicks, Set<String> tags, Map<Integer, ItemStack> inventory, Map<Integer, ItemStack> enderChest, Map<Integer, ItemStack> armor, Map<Integer, ItemStack> offhand, int selectedSlot, float absorptionAmount, float fallDistance, ExperienceData experienceData, StandPowerData standPowerData, NonPowerData nonPowerData, LivingEntity target, boolean restored) {
        this.entity = entity;
        this.position = position;
        this.health = health;
        this.lookData = lookData;
        this.fireTicks = fireTicks;
        this.tags = tags;
        this.inventory = inventory;
        this.enderChest = enderChest;
        this.armor = armor;
        this.offhand = offhand; 
        this.absorptionAmount = absorptionAmount;
        this.fallDistance = fallDistance;
        this.experienceData = experienceData;
        this.selectedSlot = selectedSlot;
        this.standPowerData = standPowerData;
        this.nonPowerData = nonPowerData;
        this.target = target;
        this.restored = restored;
    }

    public static void rewindLivingEntityData(LivingEntityData livingEntityData) {
        if (livingEntityData.restored) return;
        LivingEntity entity = livingEntityData.entity;
        if (entity.deathTime >= 1) {
            entity.deathTime = 0;
            AddonPackets.sendToClientsTrackingAndSelf(new TrResetDeathTimePacket(entity.getId()), entity);
        }
        entity.fallDistance = livingEntityData.fallDistance;
        entity.setHealth(livingEntityData.health);
        MCUtil.runCommand(entity, "tp @s " + livingEntityData.position.x() + " " + livingEntityData.position.y() + " " + livingEntityData.position.z() + " " + livingEntityData.lookData.getLookVecY() + " " + livingEntityData.lookData.getLookVecX());
        entity.setRemainingFireTicks(livingEntityData.fireTicks);
        entity.getTags().clear();
        entity.getTags().addAll(livingEntityData.tags);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.getFoodData().setFoodLevel((int) livingEntityData.absorptionAmount);
            if (TPARConfig.getSaveItems(player.level.isClientSide())) {
                InventorySaver.loadPlayerArmor(player, livingEntityData.armor);
                InventorySaver.loadPlayerInventory(player, livingEntityData.inventory);
                InventorySaver.loadPlayerChestInventory(player, livingEntityData.enderChest);
                InventorySaver.loadPlayerOffhand(player, livingEntityData.offhand);
                InventorySaver.loadSelectedSlot(player, livingEntityData.selectedSlot);
            }
            ExperienceData.loadExperienceData(player, livingEntityData.experienceData);
        }
        if (entity instanceof MobEntity && ((MobEntity) entity).getTarget() != livingEntityData.target) {
            MobEntity sigma = (MobEntity) entity;
            sigma.setTarget(livingEntityData.target);
        }
        StandPowerData.loadData(entity, livingEntityData.standPowerData);
        NonPowerData.loadData(entity, livingEntityData.nonPowerData);
        livingEntityData.restored = true;
    }

    public static LivingEntityData saveLivingEntityData(LivingEntity entity) {
        IStandPower stand = IStandPower.getStandPowerOptional(entity).orElse(null);
        StandPowerData standPowerData = null;
        INonStandPower nonPower = INonStandPower.getNonStandPowerOptional(entity).orElse(null);
        NonPowerData nonPowerData = null;
        if (stand != null && TPARConfig.getSaveStandStats(entity.level.isClientSide())) {
            standPowerData = new StandPowerData(stand, stand.isActive());
        }
        if (nonPower != null && TPARConfig.getSaveNonPowerStats(entity.level.isClientSide())) {
            nonPowerData = new NonPowerData(nonPower, nonPower.getEnergy());
        }
        return new LivingEntityData(
                entity,
                entity.position(),
                entity.getHealth(),
                new LookData(entity.xRot, entity.yRot),
                entity.getRemainingFireTicks(),
                entity.getTags(),
                InventorySaver.savePlayerInventory(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.savePlayerEnderChestInventory(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.savePlayerArmor(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.savePlayerOffhand(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                // InventorySaver.saveCarriedItem(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.saveSelectedSlot(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                entity instanceof PlayerEntity ? ((PlayerEntity) entity).getFoodData().getFoodLevel() : 0,
                entity.fallDistance,
                entity instanceof PlayerEntity ? new ExperienceData(((PlayerEntity) entity).experienceLevel, ExperienceData.getExperiencePoints((PlayerEntity) entity)) : null,
                standPowerData, nonPowerData, 
                (entity instanceof MobEntity ? ((MobEntity) entity).getTarget() : null), false);
    }

    public static void rewindDeadLivingEntityData(LivingEntityData livingEntityData, World level) {
        if (!TPARConfig.getRewindDeadLivingEntities(level.isClientSide()) || !(livingEntityData.entity instanceof MobEntity) || livingEntityData.restored) return;
    
        EntityType<?> entityType = livingEntityData.entity.getType();
        if (entityType == null) {
            return;
        }
        LivingEntity newEntity = (LivingEntity) entityType.create(level);
        if (newEntity == null) {
            return;
        }
    
        livingEntityData.entity = newEntity;
        level.addFreshEntity(newEntity);
        rewindLivingEntityData(livingEntityData);
    }

    @Override
    public Entity getEntity(IEntityData<LivingEntityData, LivingEntity> data) {
        return ((LivingEntityData) data).entity;
    }

    @Override
    public void rewindData(IEntityData<LivingEntityData, LivingEntity> data) {
        if (((LivingEntityData) data).isRestored(data)) return;
        rewindLivingEntityData((LivingEntityData) data);
    }

    @Override
    public boolean isRestored(IEntityData<LivingEntityData, LivingEntity> data) {
        return ((LivingEntityData) data).restored;
    }

    @Override
    public void rewindDeadData(IEntityData<LivingEntityData, LivingEntity> data, World level) {
        if (((LivingEntityData) data).isRestored(data)) return;
        rewindDeadLivingEntityData(((LivingEntityData) data), level);
    }

    @Override
    public boolean inData(Queue<IEntityData<LivingEntityData, LivingEntity>> livData, LivingEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        livData.forEach(data -> {
            if (data.getEntity(data) == entity) {
                inData.set(true);
            }
        });
        return inData.get();
    }

    // @Override
    // public void rewindClientData(IEntityData<LivingEntityData, LivingEntity> data) {
    //     LivingEntity entity = (LivingEntity) data.getEntity(data);
    //     LivingEntityData livingEntityData = (LivingEntityData) data;
    //     if (entity instanceof PlayerEntity) {
    //         PlayerEntity player = (PlayerEntity) entity;
    //         if (player.inventory.getCarried() != livingEntityData.carriedItem) {
    //             InventorySaver.loadCarriedItem(player, livingEntityData.carriedItem);
    //         }
    //     }
    // }
}