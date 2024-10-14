package uk.meow.weever.rotp_mandom.init;

import uk.meow.weever.rotp_mandom.MandomAddon;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITIES, MandomAddon.MOD_ID);

}
