package uk.meow.weever.rotp_mandom.data.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.data.global.BlockInventorySaver;

public class BlockData {
    public final BlockPos pos;
    public final BlockState blockState;
    public final Map<Integer, ItemStack> inventory;
    public BlockInfo blockInfo;

    public BlockData(BlockPos pos, BlockState blockState, Map<Integer, ItemStack> inventory, BlockInfo blockInfo) {
        this.pos = pos;
        this.blockState = blockState;
        this.inventory = inventory;
        this.blockInfo = blockInfo;
    }

    public static boolean inData(Queue<BlockData> blockDataQueue, BlockState blockState, BlockPos blockPos, BlockInfo newBlockInfo) {
        for (BlockData data : blockDataQueue) {
            if (data.pos.equals(blockPos) && data.blockState.equals(blockState)) {
                if (newBlockInfo.ordinal() > data.blockInfo.ordinal()) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static BlockData saveBlockData(World level, BlockState blockState, BlockPos blockPos, BlockInfo blockInfo) {
        Map<Integer, ItemStack> inventory = new HashMap<>();

        TileEntity tileEntity = level.getBlockEntity(blockPos);
        if (tileEntity != null && tileEntity instanceof IInventory) {
            inventory = BlockInventorySaver.saveBlockInventory((IInventory) tileEntity);
        }
        return new BlockData(
            blockPos, blockState, 
            inventory,
            blockInfo
        );
    }

    public static enum BlockInfo {
        BREAKED,
        PLACED,
        INTERACTED
    }
}
