package uk.meow.weever.rotp_mandom.data.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

@SuppressWarnings("unused")
@Deprecated
public class BlockData {
    private final ServerWorld world;
    public final BlockPos pos;
    private final BlockState blockState;
    public boolean restored;

    public BlockData(ServerWorld world, BlockPos pos, BlockState blockState, boolean restored) {
        this.world = world;
        this.pos = pos;
        this.blockState = blockState;
        this.restored = restored;
    }

    public static void rewindBlockData(BlockData blockData) {
        ServerWorld world = blockData.world;
        BlockPos pos = blockData.pos;
        BlockState blockState = blockData.blockState;
        world.setBlockAndUpdate(pos, blockState);
        blockData.restored = true;
//        Entity entity = entityData.entity;
//        entity.moveTo(entityData.position);
//        entityData.restored = true;
    }

    public static BlockData saveBlockData(ServerWorld world, BlockPos pos, BlockState blockState) {
        return new BlockData(
                world,
                pos,
                blockState,
                false
        );
    }

    private static boolean blockCanBePlaced(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isReplaceable();
    }
}
