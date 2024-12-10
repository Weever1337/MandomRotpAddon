package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class LivingEntityUtilCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(LivingEntityUtilCap.class)
    public static Capability<LivingEntityUtilCap> CAPABILITY = null;
    private final LazyOptional<LivingEntityUtilCap> instance;

    public LivingEntityUtilCapProvider(LivingEntity entity) {
        instance = LazyOptional.of(() -> new LivingEntityUtilCap(entity));
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
