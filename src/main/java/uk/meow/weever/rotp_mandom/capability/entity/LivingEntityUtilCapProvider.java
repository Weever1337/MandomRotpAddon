package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class LivingEntityUtilCapProvider implements ICapabilityProvider {
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
}
