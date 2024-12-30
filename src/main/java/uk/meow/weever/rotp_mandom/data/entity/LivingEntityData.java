package uk.meow.weever.rotp_mandom.data.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;

import net.minecraft.entity.Entity;
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
import uk.meow.weever.rotp_mandom.data.global.ExperienceData;
import uk.meow.weever.rotp_mandom.data.global.InventorySaver;
import uk.meow.weever.rotp_mandom.data.global.LookData;
import uk.meow.weever.rotp_mandom.data.global.NonPowerData;
import uk.meow.weever.rotp_mandom.data.global.StandPowerData;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.TrResetDeathTimePacket;

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
    private final ItemStack mainHand;
    private final ItemStack offhand;
    private final Map<Integer, ItemStack> craftingGrid;
    private final float absorptionAmount;
    private final float fallDistance;
    private final ExperienceData experienceData;
    @Nullable private final StandPowerData standPowerData;
    @Nullable private final NonPowerData nonPowerData;
    @Nullable private final LivingEntity target;
    private final Entity vehicle;
    public boolean restored;

    public LivingEntityData(LivingEntity entity, Vector3d position, float health, LookData lookData, int fireTicks, int airSupply, RegistryKey<World> dimension, Set<String> tags, List<EffectInstance> effects, Map<Integer, ItemStack> inventory, Map<Integer, ItemStack> enderChest, Map<Integer, ItemStack> armor, ItemStack mainHand, ItemStack offhand, Map<Integer, ItemStack> craftingGrid, float absorptionAmount, float fallDistance, ExperienceData experienceData, StandPowerData standPowerData, NonPowerData nonPowerData, LivingEntity target, Entity vehicle, boolean restored) {
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
        this.mainHand = mainHand;
        this.offhand = offhand; 
        this.craftingGrid = craftingGrid;
        this.absorptionAmount = absorptionAmount;
        this.fallDistance = fallDistance;
        this.experienceData = experienceData;
        this.standPowerData = standPowerData;
        this.nonPowerData = nonPowerData;
        this.target = target;
		this.vehicle = vehicle;
        this.restored = restored;
    }

    public void rewindLivingEntityData() {
        if (restored || entity.level.isClientSide() || !entity.level.dimension().equals(dimension)) {
            System.out.println("returned " + entity.getName().getString());
            return;
        }
        if (entity.deathTime > 0) {
            entity.deathTime = 0;
            AddonPackets.sendToClientsTrackingAndSelf(new TrResetDeathTimePacket(entity.getId()), entity);
        }

        entity.fallDistance = fallDistance;
        entity.setHealth(health);
        String executeCommand = "tp @s " + position.x() + " " // TODO: fix dimensions
                + position.y() + " "
                + position.z() + " "
                + lookData.lookVecY + " " + lookData.lookVecX;
        MCUtil.runCommand(entity, executeCommand);

        entity.setAirSupply(airSupply);
        entity.setRemainingFireTicks(fireTicks);
        entity.getTags().clear();
        entity.getTags().addAll(tags);

        if (!entity.getActiveEffects().isEmpty()) {
            List<EffectInstance> activeEffectsCopy = new ArrayList<>(entity.getActiveEffects());
            for (EffectInstance effect : activeEffectsCopy) {
                entity.removeEffect(effect.getEffect());
            }
        }

        if (!effects.isEmpty()) {
            for (EffectInstance savedEffect : effects) {
                System.out.println(savedEffect.getEffect().getDisplayName().getString() + " | " + savedEffect.getAmplifier() + " | " + savedEffect.getDuration());
                entity.addEffect(savedEffect);
            }
        }

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.getFoodData().setFoodLevel((int) absorptionAmount);

            InventorySaver.loadPlayerInventory(player, inventory);
            InventorySaver.loadPlayerChestInventory(player, enderChest);

            InventorySaver.loadCraftingGrid(player, craftingGrid);
            ExperienceData.loadExperienceData(player, experienceData);
        }

        InventorySaver.loadArmor(entity, armor);
        InventorySaver.loadMainhand(entity, mainHand);
        InventorySaver.loadOffhand(entity, offhand);

        if (entity instanceof MobEntity && ((MobEntity) entity).getTarget() != target) {
            MobEntity mobEntity = (MobEntity) entity;
            mobEntity.setTarget(target);
        }
        
        if (vehicle != null) {
        	entity.startRiding(vehicle);
        } else {
        	entity.stopRiding();
        }

        StandPowerData.loadData(entity, standPowerData);
        NonPowerData.loadData(entity, nonPowerData);

        restored = true;
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
                InventorySaver.saveMainhand(entity),
                InventorySaver.saveOffhand(entity),
                InventorySaver.saveCraftingGrid(entity instanceof PlayerEntity ? (PlayerEntity) entity : null),
                entity instanceof PlayerEntity ? ((PlayerEntity) entity).getFoodData().getFoodLevel() : 0,
                entity.fallDistance,
                entity instanceof PlayerEntity ? new ExperienceData(((PlayerEntity) entity).experienceLevel, ExperienceData.getExperiencePoints((PlayerEntity) entity)) : null,
                standPowerData, nonPowerData,
                (entity instanceof MobEntity ? ((MobEntity) entity).getTarget() : null), 
                entity.getVehicle(),
                false);
    }

    public void rewindDeadLivingEntityData() {
        if (!(entity instanceof MobEntity) || restored) return;
    
        EntityType<?> entityType = entity.getType();
        LivingEntity newEntity = (LivingEntity) entityType.create(entity.level);

        if (newEntity == null) {
            return;
        }
    
        entity = newEntity;
        entity.level.addFreshEntity(newEntity);
        rewindLivingEntityData();
    }
}