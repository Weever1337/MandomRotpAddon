package uk.meow.weever.rotp_mandom.init;

import com.github.standobyte.jojo.init.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.item.RingoClock;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MandomAddon.MOD_ID);

    public static final RegistryObject<RingoClock> RINGO_CLOCK = ITEMS.register("ringo_clock",
            () -> new RingoClock(new Item.Properties().tab(ModItems.MAIN_TAB).stacksTo(1)));
}