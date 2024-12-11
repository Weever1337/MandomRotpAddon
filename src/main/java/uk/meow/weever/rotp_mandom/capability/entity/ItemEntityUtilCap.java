package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;

import java.util.LinkedList;

public class ItemEntityUtilCap {
    private final ItemEntity itemEntity;
    private LinkedList<ItemData> itemData = new LinkedList<>();

    public ItemEntityUtilCap(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public void tick() {
        int maxSize = RewindConfig.getSecond(itemEntity.level.isClientSide());

        if (itemEntity.tickCount % 20 == 0) {
            if (this.itemData.size() > maxSize) {
                this.itemData.removeFirst();
            }
            addItemData(ItemData.saveItemData(itemEntity), maxSize);
        }
    }

    public LinkedList<ItemData> getItemData() {
        return itemData;
    }

    public void addItemData(ItemData itemData, int maxSize) {
        if (this.itemData.size() > maxSize) {
            this.itemData.removeFirst();
        }
        this.itemData.addLast(itemData);
    }

    public CompoundNBT toNBT() {
        return new CompoundNBT();
    }

    public void fromNBT(CompoundNBT ignored) {
    }
}
