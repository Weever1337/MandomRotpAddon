package uk.meow.weever.rotp_mandom.data.global;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockInventorySaver {
    public static Map<Integer, ItemStack> saveBlockInventory(TileEntity tileEntity) {
        Map<Integer, ItemStack> items = new HashMap<>();
        if (!(tileEntity instanceof IInventory)) return items;

        for (int i = 0; i < ((IInventory) tileEntity).getContainerSize(); i++) {
            ItemStack stack = ((IInventory) tileEntity).getItem(i);
            items.put(i, stack.copy());
        }

        return items;
    }

    public static void loadBlockInventory(TileEntity tileEntity, Map<Integer, ItemStack> savedInventory) {
        if (!(tileEntity instanceof IInventory)) return;

        for (Map.Entry<Integer, ItemStack> entry : savedInventory.entrySet()) {
            int slot = entry.getKey();
            ItemStack itemStack = entry.getValue();
            ((IInventory) tileEntity).setItem(slot, itemStack.copy());
        }
    }
}
