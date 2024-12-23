package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class ProjectileEntityUtilCapProvider implements ICapabilityProvider {
    @CapabilityInject(ProjectileEntityUtilCap.class)
    public static Capability<ProjectileEntityUtilCap> CAPABILITY = null;
    private final LazyOptional<ProjectileEntityUtilCap> instance;

    public ProjectileEntityUtilCapProvider(ProjectileEntity entity) {
        instance = LazyOptional.of(() -> new ProjectileEntityUtilCap(entity));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }
}
