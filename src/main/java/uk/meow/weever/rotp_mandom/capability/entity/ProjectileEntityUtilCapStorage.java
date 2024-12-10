package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ProjectileEntityUtilCapStorage implements Capability.IStorage<ProjectileEntityUtilCap> {

    @Override
    public INBT writeNBT(Capability<ProjectileEntityUtilCap> capability, ProjectileEntityUtilCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<ProjectileEntityUtilCap> capability, ProjectileEntityUtilCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
