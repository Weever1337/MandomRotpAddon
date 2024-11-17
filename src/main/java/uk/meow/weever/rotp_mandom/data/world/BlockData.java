package uk.meow.weever.rotp_mandom.data.world;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.data.global.BlockInventorySaver;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BlockData {
    public BlockPos pos;
    public BlockState blockState;
    public final Map<Integer, ItemStack> inventory;
    public final CompoundNBT nbt;
    public final Map<String, Comparable<?>> properties;
    public BlockInfo blockInfo;

    public BlockData(BlockPos pos, BlockState blockState, Map<Integer, ItemStack> inventory, CompoundNBT nbt, Map<String, Comparable<?>> properties, BlockInfo blockInfo) {
        this.pos = pos;
        this.blockState = blockState;
        this.inventory = inventory;
        this.nbt = nbt;
        this.properties = properties;
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
        CompoundNBT nbt = null;
        Map<String, Comparable<?>> properties = new HashMap<>();

        for (Property<?> property : blockState.getProperties()) {
            properties.put(property.getName(), blockState.getValue(property));
        }

        TileEntity tileEntity = level.getBlockEntity(blockPos);
        if (tileEntity != null) {
            if (tileEntity instanceof IInventory) {
                inventory = BlockInventorySaver.saveBlockInventory((IInventory) tileEntity);
            }
            nbt = tileEntity.serializeNBT();
        }
        return new BlockData(
            blockPos, blockState, 
            inventory,
            nbt,
            properties,
            blockInfo
        );
    }

    public static void rewindBlockData(World world, BlockData data, Set<BlockPos> processedBlocks) {
        BlockPos pos = data.pos;
        BlockState savedState = data.blockState;
        BlockState currentState = world.getBlockState(pos);
        BlockInfo blockInfo = data.blockInfo;
        
        switch (blockInfo) {
            case BREAKED:
                if (!currentState.getBlock().equals(savedState.getBlock()) || !currentState.equals(savedState)) {
                    rewindBlock(world, data);
                    processedBlocks.add(pos);
                }
                break;
            case PLACED:
                if (!processedBlocks.contains(pos)) {
                    if (currentState.getFluidState().getType() != savedState.getFluidState().getType()) {
                        world.setBlock(pos, Fluids.EMPTY.defaultFluidState().createLegacyBlock(), 3);
                    } else {
                        world.removeBlock(pos, false);
                    }
                    processedBlocks.add(pos);
                }
                break;

            case INTERACTED:
                if (!processedBlocks.contains(pos)) {
                    rewindBlock(world, data);
                    processedBlocks.add(pos);
                }
                break;
            default:
                break;
            }
    }

    public static void rewindBlock(World world, BlockData data) {
        for (Map.Entry<String, Comparable<?>> entry : data.properties.entrySet()) {
            Property<?> property = data.blockState.getBlock().getStateDefinition().getProperty(entry.getKey());
            if (property != null) {
                data.blockState = applyProperty(data.blockState, property, entry.getValue());
            }
        }
        world.setBlockAndUpdate(data.pos, data.blockState);
        TileEntity tileEntity = world.getBlockEntity(data.pos);
        if (tileEntity != null) {
            tileEntity.deserializeNBT(data.nbt);
            if (tileEntity instanceof IInventory) {
                BlockInventorySaver.loadBlockInventory((IInventory) tileEntity, data.inventory);
            }
        }
    }

    @SuppressWarnings({ "unchecked" })
    private static <T extends Comparable<T>> BlockState applyProperty(BlockState state, Property<T> property, Comparable<?> value) {
        return state.setValue(property, (T) value);
    }

    public static enum BlockInfo {
        KNOW,
        BREAKED,
        PLACED,
        INTERACTED
    }
}
