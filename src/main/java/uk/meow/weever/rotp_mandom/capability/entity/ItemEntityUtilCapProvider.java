package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class ItemEntityUtilCapProvider implements ICapabilityProvider {
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
}
