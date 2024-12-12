package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class ClientPlayerEntityUtilCapProvider implements ICapabilityProvider {
    @CapabilityInject(ClientPlayerEntityUtilCap.class)
    public static Capability<ClientPlayerEntityUtilCap> CAPABILITY = null;
    private final LazyOptional<ClientPlayerEntityUtilCap> instance;

    public ClientPlayerEntityUtilCapProvider(PlayerEntity entity) {
        instance = LazyOptional.of(() -> new ClientPlayerEntityUtilCap(entity));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CAPABILITY.orEmpty(cap, instance);
    }
}
