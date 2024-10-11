package uk.meow.weever.rotp_mandom.data.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    private static boolean blockCanBePlaced(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isReplaceable();
    }
}
