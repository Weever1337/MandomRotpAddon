package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class LivingEntityUtilCapStorage implements Capability.IStorage<LivingEntityUtilCap> {

    @Override
    public INBT writeNBT(Capability<LivingEntityUtilCap> capability, LivingEntityUtilCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<LivingEntityUtilCap> capability, LivingEntityUtilCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
