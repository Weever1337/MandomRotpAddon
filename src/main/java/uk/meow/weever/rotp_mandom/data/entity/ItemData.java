package uk.meow.weever.rotp_mandom.data.entity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Set;

public class ItemData {
    public ItemEntity entity;
    private final Vector3d position;
    private final ItemStack itemStack;
    private final Set<String> tags;
    public boolean restored;

    public ItemData(ItemEntity entity, Vector3d position, ItemStack itemStack, Set<String> tags, boolean restored) {
        this.entity = entity;
        this.position = position;
        this.itemStack = itemStack;
        this.tags = tags;
        this.restored = restored;
    }

    public static void rewindItemData(ItemData itemData) {
        ItemEntity entity = itemData.entity;
        entity.setItem(itemData.itemStack);
        entity.moveTo(itemData.position);
        entity.getTags().clear();
        entity.getTags().addAll(itemData.tags);
        itemData.setRestored(true);
    }

    public static void rewindDeadItemData(ItemData itemData, World level) {
        if (itemData.restored) return;
        ItemEntity newItemEntity = new ItemEntity(
                level,
                itemData.position.x(),
                itemData.position.y(),
                itemData.position.z(),
                itemData.itemStack
        );
        itemData.entity = newItemEntity;
        level.addFreshEntity(newItemEntity);
        rewindItemData(itemData);
        itemData.setRestored(true);
    }

    public static ItemData saveItemData(ItemEntity entity) {
        return new ItemData(
                entity,
                entity.position(),
                entity.getItem(),
                entity.getTags(),
                false
        );
    }

    public void setRestored(boolean set) {
        this.restored = set;
    }
}