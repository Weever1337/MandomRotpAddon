package uk.meow.weever.rotp_mandom.data.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockData {
    public final BlockPos pos;
    public final BlockState blockState;

    public BlockData(BlockPos pos, BlockState blockState) {
        this.pos = pos;
        this.blockState = blockState;
    }

    public static BlockData saveBlockData(BlockPos pos, BlockState blockState) {
        return new BlockData(
                pos,
                blockState
        );
    }
}
