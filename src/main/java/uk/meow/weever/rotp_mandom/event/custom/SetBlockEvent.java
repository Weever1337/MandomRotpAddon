package uk.meow.weever.rotp_mandom.event.custom;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import org.jetbrains.annotations.Nullable;
import uk.meow.weever.rotp_mandom.data.world.BlockData;

public class SetBlockEvent extends BlockEvent {
    private final BlockState oldState;
    private final BlockState newState;
    private final BlockData.TransferBlockData transferBlockData;

    public SetBlockEvent(World world, BlockPos pos, BlockState oldState, BlockState newState, BlockData.TransferBlockData transferBlockData) {
        super(world, pos, oldState);
        this.oldState = oldState;
        this.newState = newState;
        this.transferBlockData = transferBlockData;
    }

    public BlockState getNewState() {
        return newState;
    }

    @Nullable
    public BlockData.TransferBlockData getTransferedData() {
        return transferBlockData;
    }

    public boolean isNewStateIsOldState() {
        return newState == oldState;
    }
}
