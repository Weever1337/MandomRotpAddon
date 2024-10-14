package uk.meow.weever.rotp_mandom.event.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class PillagerOutpostStructureModifier extends LootModifier {
    private final Item addition;

    protected PillagerOutpostStructureModifier(ILootCondition[] conditionsIn, Item addition) {
        super(conditionsIn);
        this.addition = addition;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() > 0.9F) {
            generatedLoot.add(new ItemStack(addition, 1));
        }
        return generatedLoot;
    }


    public static class Serializer extends GlobalLootModifierSerializer<PillagerOutpostStructureModifier> {

        @Override
        public PillagerOutpostStructureModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            Item addition = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation(JSONUtils.getAsString(object, "addition")));
            return new PillagerOutpostStructureModifier(conditionsIn, addition);
        }

        @Override
        public JsonObject write(PillagerOutpostStructureModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("addition", ForgeRegistries.ITEMS.getKey(instance.addition).toString());
            return json;
        }
    }
}