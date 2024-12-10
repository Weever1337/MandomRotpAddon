package uk.meow.weever.rotp_mandom.capability.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class WorldUtilCapStorage implements Capability.IStorage<WorldUtilCap> {
    @Override
    public INBT writeNBT(Capability<WorldUtilCap> capability, WorldUtilCap instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("ServerData", instance.save());
        return nbt;
    }

    @Override
    public void readNBT(Capability<WorldUtilCap> capability, WorldUtilCap instance, Direction side, INBT nbt) {
        CompoundNBT cnbt = (CompoundNBT) nbt;
        instance.load(cnbt.getCompound("ServerData"));
    }
}
