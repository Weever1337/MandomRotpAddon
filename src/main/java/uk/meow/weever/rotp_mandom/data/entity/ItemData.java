package uk.meow.weever.rotp_mandom.data.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemData implements IEntityData<ItemData, ItemEntity> {
    public ItemEntity entity;
    private final Vector3d position;
    private final ItemStack itemStack;
    private final Set<String> tags;
    private CompoundNBT nbt;
    public boolean restored;

    public ItemData(ItemEntity entity, Vector3d position, ItemStack itemStack, Set<String> tags, CompoundNBT nbt, boolean restored) {
        this.entity = entity;
        this.position = position;
        this.itemStack = itemStack;
        this.tags = tags;
        this.nbt = nbt;
        this.restored = restored;
    }

    public static void rewindItemData(ItemData itemData) {
        ItemEntity entity = itemData.entity;
        entity.setItem(itemData.itemStack);
        entity.moveTo(itemData.position);
        entity.getTags().clear();
        entity.getTags().addAll(itemData.tags);
        entity.getItem().deserializeNBT(itemData.nbt);
        itemData.restored = true;
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
        rewindItemData(itemData);
        level.addFreshEntity(newItemEntity);
    }

    public static ItemData saveItemData(ItemEntity entity) {
        return new ItemData(
                entity,
                entity.position(),
                entity.getItem(),
                entity.getTags(),
                entity.getItem().serializeNBT(),
                false
        );
    }

    @Override
    public Entity getEntity(IEntityData<ItemData, ItemEntity> data) {
        return ((ItemData)data).entity;
    }

    @Override
    public void rewindData(IEntityData<ItemData, ItemEntity> data) {
        if (((ItemData)data).restored) return;
        rewindItemData(((ItemData)data));
    }

    @Override
    public boolean isRestored(IEntityData<ItemData, ItemEntity> data) {
        return ((ItemData)data).restored;
    }

    @Override
    public void rewindDeadData(IEntityData<ItemData, ItemEntity> data, World level) {
        if (((ItemData)data).restored) return;
        rewindDeadItemData(((ItemData)data), level);
    }

    @Override
    public boolean inData(Queue<IEntityData<ItemData, ItemEntity>> itemData, ItemEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        itemData.forEach(data -> {
            if (data.getEntity(data) == entity) {
                inData.set(true);
            }
        });
        return inData.get();
    }
}