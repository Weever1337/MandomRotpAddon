package uk.meow.weever.rotp_mandom.data.entity;

import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.global.*;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.TrResetDeathTimePacket;

import java.util.*;

public class LivingEntityData {
    public LivingEntity entity;
    private final Vector3d position;
    private final float health;
    private final LookData lookData;
    private final int fireTicks;
    private final int airSupply;
    private final RegistryKey<World> dimension;
    private final Set<String> tags;
    private final List<EffectInstance> effects;
    private final Map<Integer, ItemStack> inventory;
    private final Map<Integer, ItemStack> enderChest;
    private final Map<Integer, ItemStack> armor;
    private final ItemStack offhand;
    private final Map<Integer, ItemStack> craftingGrid;
    private final int selectedSlot;
    private final float absorptionAmount;
    private final float fallDistance;
    private final ExperienceData experienceData;
    private final StandPowerData standPowerData;
    private final NonPowerData nonPowerData;
    private final LivingEntity target;
    public boolean restored;

    public LivingEntityData(LivingEntity entity, Vector3d position, float health, LookData lookData, int fireTicks, int airSupply, RegistryKey<World> dimension, Set<String> tags, List<EffectInstance> effects, Map<Integer, ItemStack> inventory, Map<Integer, ItemStack> enderChest, Map<Integer, ItemStack> armor, ItemStack offhand, Map<Integer, ItemStack> craftingGrid, int selectedSlot, float absorptionAmount, float fallDistance, ExperienceData experienceData, StandPowerData standPowerData, NonPowerData nonPowerData, LivingEntity target, boolean restored) {
        this.entity = entity;
        this.position = position;
        this.health = health;
        this.lookData = lookData;
        this.fireTicks = fireTicks;
        this.airSupply = airSupply;
        this.dimension = dimension;
        this.tags = tags;
        this.effects = effects;
        this.inventory = inventory;
        this.enderChest = enderChest;
        this.armor = armor;
        this.offhand = offhand; 
        this.craftingGrid = craftingGrid;
        this.selectedSlot = selectedSlot;
        this.absorptionAmount = absorptionAmount;
        this.fallDistance = fallDistance;
        this.experienceData = experienceData;
        this.standPowerData = standPowerData;
        this.nonPowerData = nonPowerData;
        this.target = target;
        this.restored = restored;
    }

    public void rewindLivingEntityData(LivingEntityData livingEntityData) {
        if (livingEntityData.restored) return;
        LivingEntity entity = livingEntityData.entity;
        if (entity.deathTime > 0) {
            entity.deathTime = 0;
            AddonPackets.sendToClientsTrackingAndSelf(new TrResetDeathTimePacket(entity.getId()), entity);
        }

        entity.fallDistance = livingEntityData.fallDistance;
        entity.setHealth(livingEntityData.health);
        MCUtil.runCommand(entity, "execute in " + livingEntityData.dimension.location() + " run tp @s " + livingEntityData.position.x() + " " + livingEntityData.position.y() + " " + livingEntityData.position.z() + " " + livingEntityData.lookData.lookVecY + " " + livingEntityData.lookData.lookVecX);

        entity.setAirSupply(livingEntityData.airSupply);
        entity.setRemainingFireTicks(livingEntityData.fireTicks);
        entity.getTags().clear();
        entity.getTags().addAll(livingEntityData.tags);

        for (EffectInstance effect : entity.getActiveEffects()) {
            entity.removeEffect(effect.getEffect());
        }

        for (EffectInstance savedEffect : livingEntityData.effects) {
            System.out.println(savedEffect.getEffect().getDisplayName().getString() + " | " + savedEffect.getAmplifier() + " | " + savedEffect.getDuration());
            boolean added = entity.addEffect(savedEffect);
            System.out.println(added);
        }

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.getFoodData().setFoodLevel((int) livingEntityData.absorptionAmount);

            InventorySaver.loadPlayerInventory(player, livingEntityData.inventory);
            InventorySaver.loadPlayerChestInventory(player, livingEntityData.enderChest);

            InventorySaver.loadSelectedSlot(player, livingEntityData.selectedSlot);
            InventorySaver.loadCraftingGrid(player, livingEntityData.craftingGrid);

            ExperienceData.loadExperienceData(player, livingEntityData.experienceData);
        }

        InventorySaver.loadArmor(entity, livingEntityData.armor);
        InventorySaver.loadOffhand(entity, livingEntityData.offhand);

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

        if (stand != null && RewindConfig.getSaveStandStats(entity.level.isClientSide())) {
            standPowerData = new StandPowerData(stand, stand.isActive());
        }

        if (nonPower != null && RewindConfig.getSaveNonPowerStats(entity.level.isClientSide())) {
            nonPowerData = new NonPowerData(nonPower, nonPower.getEnergy());
        }
        
        return new LivingEntityData(
                entity,
                entity.position(),
                entity.getHealth(),
                new LookData(entity.xRot, entity.yRot),
                entity.getRemainingFireTicks(),
                entity.getAirSupply(),
                entity.level.dimension(),
                entity.getTags(),
                new ArrayList<>(entity.getActiveEffects()),
                InventorySaver.savePlayerInventory(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.savePlayerEnderChestInventory(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.saveArmor(entity),
                InventorySaver.saveOffhand(entity),
                InventorySaver.saveCraftingGrid(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                InventorySaver.saveSelectedSlot(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                entity instanceof PlayerEntity ? ((PlayerEntity) entity).getFoodData().getFoodLevel() : 0,
                entity.fallDistance,
                entity instanceof PlayerEntity ? new ExperienceData(((PlayerEntity) entity).experienceLevel, ExperienceData.getExperiencePoints((PlayerEntity) entity)) : null,
                standPowerData, nonPowerData, 
                (entity instanceof MobEntity ? ((MobEntity) entity).getTarget() : null), false);
    }

    public void rewindDeadLivingEntityData(LivingEntityData livingEntityData, World level) {
        if (!(livingEntityData.entity instanceof MobEntity) || livingEntityData.restored) return;
    
        EntityType<?> entityType = livingEntityData.entity.getType();

        LivingEntity newEntity = (LivingEntity) entityType.create(level);

        if (newEntity == null) {
            return;
        }
    
        livingEntityData.entity = newEntity;
        level.addFreshEntity(newEntity);
        rewindLivingEntityData(livingEntityData);
    }
}