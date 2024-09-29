package uk.meow.weever.rotp_mandom.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerUtilCapStorage implements Capability.IStorage<PlayerUtilCap> {

    @Override
    public INBT writeNBT(Capability<PlayerUtilCap> capability, PlayerUtilCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<PlayerUtilCap> capability, PlayerUtilCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
