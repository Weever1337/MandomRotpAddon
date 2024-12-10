package uk.meow.weever.rotp_mandom.capability.world;

import net.minecraft.nbt.INBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WorldUtilCapProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(WorldUtilCap.class)
    public static Capability<WorldUtilCap> CAPABILITY = null;
    private LazyOptional<WorldUtilCap> instance;

    public WorldUtilCapProvider(ServerWorld world) {
        this.instance = LazyOptional.of(() -> new WorldUtilCap(world));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    @Override
    public INBT serializeNBT() {
        return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached.")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CAPABILITY.getStorage().readNBT(CAPABILITY, instance.orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached.")), null, nbt);
    }

    public static WorldUtilCap getWorldCap(ServerWorld world) {
        return world.getCapability(CAPABILITY).orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached."));
    }
}
