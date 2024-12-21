package uk.meow.weever.rotp_mandom.item;

import com.github.standobyte.jojo.client.ClientUtil;
import uk.meow.weever.rotp_mandom.config.GlobalConfig;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class RingoClock extends Item {
    public RingoClock(Properties properties) {
        super(properties);
    }

    public static int MAX_CAN_BE_CRACKED() {
        return GlobalConfig.getMaxCastRingoClock(false);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        int SEC = RewindConfig.getSecond();
        tooltip.add(new TranslationTextComponent("tooltip.rotp_mandom.ringo_clock", SEC, SEC).withStyle(TextFormatting.RED));
        ClientUtil.addItemReferenceQuote(tooltip, this);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int cracked = stack.getOrCreateTag().getInt("cracked");
        return cracked > MAX_CAN_BE_CRACKED() ? 1 : (double) cracked / MAX_CAN_BE_CRACKED();
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getOrCreateTag().getInt("cracked") >= 1;
    }
}
