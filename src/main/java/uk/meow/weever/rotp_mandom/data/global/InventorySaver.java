package uk.meow.weever.rotp_mandom.data.global;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import uk.meow.weever.rotp_mandom.network.AddonPackets;
import uk.meow.weever.rotp_mandom.network.server.RWSetSelectedSlot;

import java.util.HashMap;
import java.util.Map;

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

    public static int saveSelectedSlot(PlayerEntity player) {
        if (player == null) return -1;
        return player.inventory.selected;
    }

    public static void loadSelectedSlot(PlayerEntity player, int slot) {
        if (player == null) return;

        player.inventory.selected = slot;
        AddonPackets.sendToClient(new RWSetSelectedSlot(player.getId(), slot), player);
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

    public static Map<Integer, ItemStack> savePlayerArmor(PlayerEntity player) {
        Map<Integer, ItemStack> armorMap = new HashMap<>();
        if (player == null) return armorMap;

        NonNullList<ItemStack> armorInventory = player.inventory.armor;
        for (int i = 0; i < armorInventory.size(); i++) {
            ItemStack itemStack = armorInventory.get(i);
            if (!itemStack.isEmpty()) {
                armorMap.put(i, itemStack.copy());
            }
        }

        return armorMap;
    }

    public static void loadPlayerArmor(PlayerEntity player, Map<Integer, ItemStack> savedArmor) {
        if (player == null) return;

        NonNullList<ItemStack> armorInventory = player.inventory.armor;
        for (Map.Entry<Integer, ItemStack> entry : savedArmor.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            if (slot < armorInventory.size()) {
                armorInventory.set(slot, itemStack.copy());
            }
        }
    }

    public static Map<Integer, ItemStack> savePlayerOffhand(PlayerEntity player) {
        Map<Integer, ItemStack> offhandMap = new HashMap<>();
        if (player == null) return offhandMap;

        NonNullList<ItemStack> offhandInventory = player.inventory.offhand;
        for (int i = 0; i < offhandInventory.size(); i++) {
            ItemStack itemStack = offhandInventory.get(i);
            if (!itemStack.isEmpty()) {
                offhandMap.put(i, itemStack.copy());
            }
        }

        return offhandMap;
    }

    public static void loadPlayerOffhand(PlayerEntity player, Map<Integer, ItemStack> savedOffhand) {
        if (player == null) return;

        NonNullList<ItemStack> offhandInventory = player.inventory.offhand;
        for (Map.Entry<Integer, ItemStack> entry : savedOffhand.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            if (slot < offhandInventory.size()) {
                offhandInventory.set(slot, itemStack.copy());
            }
        }
    }
}
