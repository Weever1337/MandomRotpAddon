package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ItemEntityUtilCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(ItemEntityUtilCap.class)
    public static Capability<ItemEntityUtilCap> CAPABILITY = null;
    private final LazyOptional<ItemEntityUtilCap> instance;

    public ItemEntityUtilCapProvider(ItemEntity entity) {
        instance = LazyOptional.of(() -> new ItemEntityUtilCap(entity));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Entity capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("Entity capability LazyOptional is not attached.")), null, nbt);
    }
}
