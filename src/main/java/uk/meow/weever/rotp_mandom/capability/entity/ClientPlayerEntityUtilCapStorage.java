package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ClientPlayerEntityUtilCapStorage implements Capability.IStorage<ClientPlayerEntityUtilCap> {

    @Override
    public INBT writeNBT(Capability<ClientPlayerEntityUtilCap> capability, ClientPlayerEntityUtilCap instance, Direction side) {
        return instance.toNBT();
    }

    @Override
    public void readNBT(Capability<ClientPlayerEntityUtilCap> capability, ClientPlayerEntityUtilCap instance, Direction side, INBT nbt) {
        instance.fromNBT((CompoundNBT) nbt);
    }
}
