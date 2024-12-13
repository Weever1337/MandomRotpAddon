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
import org.jetbrains.annotations.Nullable;
import uk.meow.weever.rotp_mandom.data.global.BlockInventorySaver;

import java.util.*;

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

    public static boolean inData(List<BlockData> blockDataQueue, BlockPos blockPos) {
        for (BlockData data : blockDataQueue) {
            if (data.pos.equals(blockPos)) {
                return true;
            }
        }
        return false;
    }

    public static BlockData saveBlockData(BlockState blockState, BlockPos blockPos, BlockInfo blockInfo, TransferBlockData transferBlockData) {
        Map<Integer, ItemStack> inventory = transferBlockData.inventory;
        CompoundNBT nbt = transferBlockData.nbt;
        Map<String, Comparable<?>> properties = new HashMap<>();

        for (Property<?> property : blockState.getProperties()) {
            properties.put(property.getName(), blockState.getValue(property));
        }

        return new BlockData(
            blockPos, blockState,
            inventory,
            nbt,
            properties,
            blockInfo
        );
    }

    public static void rewindBlockData(World world, BlockData data) {
        BlockPos pos = data.pos;
        BlockState savedState = data.blockState;
        BlockState currentState = world.getBlockState(pos);
        BlockInfo blockInfo = data.blockInfo;

//        System.out.println(savedState.getBlock().getName().getString() + " | " + blockInfo.name());
//        switch (blockInfo) {
//            case BREAKED:
//                rewindBlock(world, data);
//                break;
//            case PLACED:
//                if (currentState.getFluidState().getType() != savedState.getFluidState().getType()) {
//                    world.setBlock(pos, Fluids.EMPTY.defaultFluidState().createLegacyBlock(), 3);
//                } else {
//                    world.removeBlock(pos, false);
//                }
//                break;
//            default:
//                break;
//            }
        switch (blockInfo) {
            case BREAKED:
                rewindBlock(world, data);
                break;
            case PLACED:
                if (currentState.getFluidState().getType() != savedState.getFluidState().getType()) {
                    world.setBlock(pos, Fluids.EMPTY.defaultFluidState().createLegacyBlock(), 3);
                } else {
                    world.removeBlock(pos, false);
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
                BlockInventorySaver.loadBlockInventory(tileEntity, data.inventory);
            }
        }
    }

    @SuppressWarnings({ "unchecked" })
    private static <T extends Comparable<T>> BlockState applyProperty(BlockState state, Property<T> property, Comparable<?> value) {
        return state.setValue(property, (T) value);
    }

    public enum BlockInfo {
        BREAKED,
        PLACED
    }

    public static class TransferBlockData {
        public final BlockState blockState;
        @Nullable
        public final Map<Integer, ItemStack> inventory;
        @Nullable
        public final CompoundNBT nbt;

        public TransferBlockData(BlockState blockState, @Nullable Map<Integer, ItemStack> inventory, @Nullable CompoundNBT nbt) {
            this.blockState = blockState;
            this.inventory = inventory;
            this.nbt = nbt;
        }
    }
}
