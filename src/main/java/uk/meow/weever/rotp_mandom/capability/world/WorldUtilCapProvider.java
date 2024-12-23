package uk.meow.weever.rotp_mandom.capability.world;

import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class WorldUtilCapProvider implements ICapabilityProvider {
    @CapabilityInject(WorldUtilCap.class)
    public static Capability<WorldUtilCap> CAPABILITY = null;
    private LazyOptional<WorldUtilCap> instance;

    public WorldUtilCapProvider(World world) {
        this.instance = LazyOptional.of(() -> new WorldUtilCap(world));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }

    public static WorldUtilCap getWorldCap(World world) {
        return world.getCapability(CAPABILITY).orElseThrow(
                () -> new IllegalArgumentException("World capability LazyOptional is not attached."));
    }
}
