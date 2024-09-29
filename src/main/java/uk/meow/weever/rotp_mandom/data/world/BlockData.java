package uk.meow.weever.rotp_mandom.data.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

@SuppressWarnings("unused")
public class BlockData {
    private final ServerWorld world;
    private final BlockPos pos;
    private final BlockState blockState;
    private final Block block;

    public BlockData(ServerWorld world, BlockPos pos, BlockState blockState, Block block) {
        this.world = world;
        this.pos = pos;
        this.blockState = blockState;
        this.block = block;
    }

    public ServerWorld getWorld() {
        return world;
    }
}
