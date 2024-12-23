package uk.meow.weever.rotp_mandom.data.global;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.RWSetSelectedSlot;

import java.util.*;

public class InventorySaver {
    public static Map<Integer, ItemStack> savePlayerInventory(PlayerEntity player) {
        Map<Integer, ItemStack> inventoryMap = new HashMap<>();
        if (player == null) return inventoryMap;

        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack itemStack = player.inventory.getItem(i);
            inventoryMap.put(i, itemStack.copy());
        }

        return inventoryMap;
    }

    public static void loadPlayerInventory(PlayerEntity player, Map<Integer, ItemStack> savedInventory) {
        if (player == null) return;

        for (Map.Entry<Integer, ItemStack> entry : savedInventory.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            player.inventory.setItem(slot, itemStack.copy());
        }
    }

    public static ItemStack saveCarriedItem(PlayerEntity player) {
        if (player == null) return null;
        return player.inventory.getCarried();
    }

    public static void loadCarriedItem(PlayerEntity player, ItemStack itemStack) {
        if (player == null) return;

        player.inventory.setCarried(itemStack);
    }

    public static int saveSelectedSlot(PlayerEntity player) {
        if (player == null) return -1;
        return player.inventory.selected;
    }

    public static void loadSelectedSlot(PlayerEntity player, int slot) {
        if (player == null) return;

        player.inventory.selected = slot;
        if (!player.level.isClientSide()) {
            AddonPackets.sendToClient(new RWSetSelectedSlot(player.getId(), slot), player);
        }
    }

    public static Map<Integer, ItemStack> savePlayerEnderChestInventory(PlayerEntity player) {
        Map<Integer, ItemStack> enderChestInventoryMap = new HashMap<>();
        if (player == null) return enderChestInventoryMap;

        for (int i = 0; i < player.getEnderChestInventory().getContainerSize(); i++) {
            ItemStack itemStack = player.getEnderChestInventory().getItem(i);
            enderChestInventoryMap.put(i, itemStack.copy());
        }

        return enderChestInventoryMap;
    }

    public static void loadPlayerChestInventory(PlayerEntity player, Map<Integer, ItemStack> savedEnderChestInventory) {
        if (player == null) return;

        for (Map.Entry<Integer, ItemStack> entry : savedEnderChestInventory.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            player.getEnderChestInventory().setItem(slot, itemStack.copy());
        }
    }

    public static Map<Integer, ItemStack> saveArmor(LivingEntity livingEntity) {
        Map<Integer, ItemStack> armorMap = new HashMap<>();
        if (livingEntity == null) return armorMap;
        Iterable<ItemStack> iterable = livingEntity.getArmorSlots();

        List<ItemStack> armorInventory = new ArrayList<>((Collection<? extends ItemStack>) iterable);
        for (int i = 0; i < armorInventory.size(); i++) {
            ItemStack itemStack = armorInventory.get(i);
            if (!itemStack.isEmpty()) {
                armorMap.put(i, itemStack.copy());
            }
        }

        return armorMap;
    }

    public static void loadArmor(LivingEntity livingEntity, Map<Integer, ItemStack> savedArmor) {
        if (livingEntity == null) return;

        Iterable<ItemStack> iterable = livingEntity.getArmorSlots();
        List<ItemStack> armorInventory = new ArrayList<>((Collection<? extends ItemStack>) iterable);
        for (Map.Entry<Integer, ItemStack> entry : savedArmor.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            if (slot < armorInventory.size()) {
                armorInventory.set(slot, itemStack.copy());
            }
        }
    }

    public static ItemStack saveMainhand(LivingEntity livingEntity) {
        if (!(livingEntity instanceof MobEntity)) return null;

        return livingEntity.getMainHandItem();
    }

    public static void loadMainhand(LivingEntity livingEntity, ItemStack savedOffhand) {
        if (!(livingEntity instanceof MobEntity)) return;

        livingEntity.setItemInHand(Hand.MAIN_HAND, savedOffhand);
    }

    public static ItemStack saveOffhand(LivingEntity livingEntity) {
        if (livingEntity == null) return null;

        return livingEntity.getOffhandItem();
    }

    public static void loadOffhand(LivingEntity livingEntity, ItemStack savedOffhand) {
        if (livingEntity == null) return;

        livingEntity.setItemInHand(Hand.OFF_HAND, savedOffhand);
    }

    public static Map<Integer, ItemStack> saveCraftingGrid(PlayerEntity player) {
        Map<Integer, ItemStack> craftingGridMap = new HashMap<>();
        if (player == null) return craftingGridMap;

        CraftingInventory craftingInventory = player.inventoryMenu.getCraftSlots();
        for (int i = 0; i < craftingInventory.getContainerSize(); i++) {
            ItemStack itemStack = craftingInventory.getItem(i);
            craftingGridMap.put(i, itemStack.copy());
        }

        return craftingGridMap;
    }

    public static void loadCraftingGrid(PlayerEntity player, Map<Integer, ItemStack> savedCraftingGrid) {
        if (player == null) return;

        CraftingInventory craftingInventory = player.inventoryMenu.getCraftSlots();
        for (Map.Entry<Integer, ItemStack> entry : savedCraftingGrid.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            if (slot < craftingInventory.getContainerSize()) {
                craftingInventory.setItem(slot, itemStack.copy());
            }
        }
    }
}