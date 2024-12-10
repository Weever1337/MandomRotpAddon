package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ItemEntityUtilCapStorage implements Capability.IStorage<ItemEntityUtilCap> {

    @Override
    public INBT writeNBT(Capability<ItemEntityUtilCap> capability, ItemEntityUtilCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<ItemEntityUtilCap> capability, ItemEntityUtilCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
