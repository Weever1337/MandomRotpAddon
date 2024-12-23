package uk.meow.weever.rotp_mandom.data.entity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import java.util.Set;

public class ItemData {
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

    public void rewindItemData(ItemData itemData) {
        ItemEntity entity = itemData.entity;
        entity.setItem(itemData.itemStack);
        entity.moveTo(itemData.position);
        entity.getTags().clear();
        entity.getTags().addAll(itemData.tags);
        entity.getItem().deserializeNBT(itemData.nbt);
        itemData.restored = true;
    }

    public void rewindDeadItemData(ItemData itemData, World level) {
        if (itemData.restored) return;
        ItemEntity newEntity = new ItemEntity(
                level,
                itemData.position.x(),
                itemData.position.y(),
                itemData.position.z(),
                itemData.itemStack
        );
//        EntityType<?> entityType = itemData.entity.getType();
//        ItemEntity newEntity = (ItemEntity) entityType.create(level);
//
//        System.out.println(newEntity == null);
//        if (newEntity == null) {
//            return;
//        }

        itemData.entity = newEntity;
        level.addFreshEntity(newEntity);
        rewindItemData(itemData);
        System.out.println("WHAAAAAT: " + newEntity.getName().getString());
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
}