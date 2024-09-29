package uk.meow.weever.rotp_mandom.data.entity;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.config.TPARConfig;
import uk.meow.weever.rotp_mandom.data.global.ExperienceData;
import uk.meow.weever.rotp_mandom.data.global.InventorySaver;
import uk.meow.weever.rotp_mandom.data.global.LookData;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.TrResetDeathTimePacket;

import java.util.*;

public class LivingEntityData {
    private final Map<Integer, ItemStack> inventory;
    private final Vector3d position;
    private final float health;
    private final LookData lookData;
    private final int fireTicks;
    private final Set<String> tags;
    private final Map<Integer, ItemStack> armor;
    private final Map<Integer, ItemStack> offhand;
    public LivingEntity entity;
    private float absorptionAmount;
    private float fallDistance;
    private ExperienceData experienceData;
    public boolean restored;

    public LivingEntityData(LivingEntity entity, Vector3d position, float health, LookData lookData, int fireTicks, Set<String> tags, Map<Integer, ItemStack> inventory, Map<Integer, ItemStack> armor, Map<Integer, ItemStack> offhand, float absorptionAmount, float fallDistance, ExperienceData experienceData, boolean restored) {
        this.entity = entity;
        this.position = position;
        this.health = health;
        this.lookData = lookData;
        this.fireTicks = fireTicks;
        this.tags = tags;
        this.inventory = inventory;
        this.armor = armor;
        this.offhand = offhand;
        this.absorptionAmount = absorptionAmount;
        this.fallDistance = fallDistance;
        this.experienceData = experienceData;
        this.restored = restored;
    }

    public static void rewindLivingEntityData(LivingEntityData livingEntityData) {
        LivingEntity entity = livingEntityData.entity;
        entity.deathTime = 0;
        AddonPackets.sendToClientsTrackingAndSelf(new TrResetDeathTimePacket(entity.getId()), entity);
        entity.fallDistance = livingEntityData.fallDistance;
        entity.setHealth(livingEntityData.health);
        MCUtil.runCommand(entity, "tp @s " + livingEntityData.position.x() + " " + livingEntityData.position.y() + " " + livingEntityData.position.z() + " " + livingEntityData.lookData.getLookVecY() + " " + livingEntityData.lookData.getLookVecX());
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.getFoodData().setFoodLevel((int) livingEntityData.absorptionAmount);
            if (TPARConfig.getSaveInventory(player.level.isClientSide())) {
                InventorySaver.loadPlayerArmor(player, livingEntityData.armor);
                InventorySaver.loadPlayerInventory(player, livingEntityData.inventory);
                InventorySaver.loadPlayerOffhand(player, livingEntityData.offhand);
            }
            // if (livingEntityData.experienceData != null) {
            //     System.out.println("Experience data: " + livingEntityData.experienceData.level + " levels, " + livingEntityData.experienceData.points + " points, " + livingEntityData.experienceData.total + " total");
            //     System.out.println("Points: " + livingEntityData.experienceData.points * 10);
            //     MCUtil.runCommand(player, "experience set @s " + livingEntityData.experienceData.level + " levels");
            //     MCUtil.runCommand(player, "experience set @s 0 points");
            //     System.out.println((int) livingEntityData.experienceData.points);
            //     player.giveExperiencePoints((int) livingEntityData.experienceData.points);
            //     // player.giveExperiencePoints((int) livingEntityData.experienceData.points);
            //     // player.experienceLevel = livingEntityData.experienceData.level;
            //     // player.experienceProgress = livingEntityData.experienceData.points;
            //     // player.totalExperience = livingEntityData.experienceData.total;
            // }
        }
        entity.setRemainingFireTicks(livingEntityData.fireTicks);
        entity.getTags().clear();
        entity.getTags().addAll(livingEntityData.tags);
        livingEntityData.restored = true;
    }

    public static LivingEntityData saveLivingEntityData(LivingEntity entity) {
        return new LivingEntityData(
                entity,
                entity.position(),
                entity.getHealth(),
                new LookData(entity.xRot, entity.yRot),
                entity.getRemainingFireTicks(),
                entity.getTags(),
                InventorySaver.savePlayerInventory(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.savePlayerArmor(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.savePlayerOffhand(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                entity instanceof PlayerEntity ? ((PlayerEntity) entity).getFoodData().getFoodLevel() : 0,
                entity.fallDistance,
                entity instanceof PlayerEntity ? new ExperienceData(((PlayerEntity) entity).experienceLevel, ((PlayerEntity) entity).experienceProgress, ((PlayerEntity) entity).totalExperience) : null,
                false
        );
    }

    public static void rewindDeadLivingEntityData(LivingEntityData livingEntityData, World level) {
        System.out.println("Entity: " + livingEntityData.entity.getName().getString());
        if (!TPARConfig.getRewindDeadLivingEntities(level.isClientSide()) || !(livingEntityData.entity instanceof MobEntity)) return;
    
        EntityType<?> entityType = livingEntityData.entity.getType();
        if (entityType == null) {
            System.out.println("EntityType is null for entity: " + livingEntityData.entity);
            return;
        }
        System.out.println("Level: " + level);
        System.out.println("EntityType: " + entityType);
        LivingEntity newEntity = (LivingEntity) entityType.create(level);
        if (newEntity == null) {
            System.out.println("Failed to create new entity for type: " + entityType);
            return;
        }
    
        livingEntityData.entity = newEntity;
        level.addFreshEntity(newEntity);
        rewindLivingEntityData(livingEntityData);
    }
    

    public static boolean ifForRestoringLivingEntityData(List<Entity> entitiesAround, LivingEntityData livingEntityData) {
        return entitiesAround.contains(livingEntityData.entity);
    }
}