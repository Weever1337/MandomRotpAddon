package uk.meow.weever.rotp_mandom.capability.entity;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ItemData;
import uk.meow.weever.rotp_mandom.util.AddonUtil;

import java.util.LinkedList;

public class ItemEntityUtilCap {
    private final ItemEntity itemEntity;
    private final LinkedList<ItemData> itemData = new LinkedList<>();

    public ItemEntityUtilCap(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public void tick() {
        int maxSize = RewindConfig.getSecond();

        if (itemEntity.tickCount % 20 == 0) {
            if(TimeStopHandler.isTimeStopped(itemEntity.level, itemEntity.blockPosition())) return;
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
}
