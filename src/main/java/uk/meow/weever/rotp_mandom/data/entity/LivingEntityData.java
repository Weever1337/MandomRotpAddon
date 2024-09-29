package uk.meow.weever.rotp_mandom.data.entity;

import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.config.TPARConfig;
import uk.meow.weever.rotp_mandom.data.global.InventorySaver;
import uk.meow.weever.rotp_mandom.data.global.LookData;

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
    public boolean restored;

    public LivingEntityData(LivingEntity entity, Vector3d position, float health, LookData lookData, int fireTicks, Set<String> tags, Map<Integer, ItemStack> inventory, Map<Integer, ItemStack> armor, Map<Integer, ItemStack> offhand, float absorptionAmount, boolean restored) {
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
        this.restored = restored;
    }

    public static void rewindLivingEntityData(LivingEntityData livingEntityData) {
        LivingEntity entity = livingEntityData.entity;
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
//                entity instanceof PlayerEntity ? new ExperienceData(((PlayerEntity) entity).experienceLevel, ((PlayerEntity) entity).experienceProgress) : null,
                false
        );
    }

    public static void rewindDeadLivingEntityData(LivingEntityData livingEntityData, World level) {
        if (!TPARConfig.getRewindDeadLivingEntities(level.isClientSide())) return;
        EntityType<?> entityType = livingEntityData.entity.getType();
        LivingEntity newEntity = (LivingEntity) entityType.create(level);
        if (newEntity == null) return;
        livingEntityData.entity = newEntity;
        level.addFreshEntity(newEntity);
        rewindLivingEntityData(livingEntityData);
    }

    public static boolean ifForRestoringLivingEntityData(List<Entity> entitiesAround, LivingEntityData livingEntityData) {
        return entitiesAround.contains(livingEntityData.entity);
    }
}