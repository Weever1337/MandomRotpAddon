package uk.meow.weever.rotp_mandom.data.global;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BlockInventorySaver {
    public static Map<Integer, ItemStack> saveBlockInventory(IInventory inventory) {
        Map<Integer, ItemStack> items = new HashMap<>();
        if (inventory == null) return items;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            items.put(i, itemStack.copy());
        }

        return items;
    }

    public static void loadBlockInventory(IInventory inventory, Map<Integer, ItemStack> savedInventory) {
        if (inventory == null) return;

        for (Map.Entry<Integer, ItemStack> entry : savedInventory.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            inventory.setItem(slot, itemStack.copy());
        }
    }

    // public static CompoundNBT saveBlockInventory(World world, BlockPos pos) {
    //     TileEntity tileEntity = world.getBlockEntity(pos);
    //     if (tileEntity instanceof IInventory) {
    //         IInventory inventory = (IInventory) tileEntity;
    //         return saveBlockInventory(inventory, nbt);
    //     }
    //     return nbt;
    // }

    // public static void loadBlockInventory(World world, BlockPos pos) {
    //     TileEntity tileEntity = world.getBlockEntity(pos);
    //     if (tileEntity instanceof IInventory) {
    //         IInventory inventory = (IInventory) tileEntity;
    //         loadInventoryFromNBT(inventory, nbt);
    //     }
    // }
}
