package uk.meow.weever.rotp_mandom.mixin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import uk.meow.weever.rotp_mandom.data.global.BlockInventorySaver;
import uk.meow.weever.rotp_mandom.data.world.BlockData;
import uk.meow.weever.rotp_mandom.event.custom.SetBlockEvent;

@Mixin(value=World.class, remap=false)
public abstract class WorldMixin {
    @Unique
    private static final Map<BlockPos, BlockData.TransferBlockData> mandomRotpAddon$handledPositions = new HashMap<>();

    @Inject(
        method = "setBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER)
    )
    public void beforeSetBlock(BlockPos pos, BlockState newState, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> cir) {
        World world = (World) (Object) this;
        if (!world.isClientSide()) {
            BlockState oldState = world.getBlockState(pos);
            if (oldState == null) return;
            TileEntity tileEntity = world.getBlockEntity(pos);

            CompoundNBT tileNbt = tileEntity != null ? tileEntity.serializeNBT() : null;
            Map<Integer, ItemStack> inventory = tileEntity instanceof IInventory
                    ? BlockInventorySaver.saveBlockInventory(tileEntity)
                    : Collections.emptyMap();

            BlockData.TransferBlockData transferBlockData = new BlockData.TransferBlockData(oldState, inventory, tileNbt);
            mandomRotpAddon$handledPositions.put(pos, transferBlockData);
        }
    }

    @Inject(
            method = "setBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
            at = @At("TAIL")
    )
    public void afterSetBlock(BlockPos pos, BlockState newState, int flags, int recursionLeft, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            World world = (World) (Object) this;
            if (!world.isClientSide()) {
                BlockData.TransferBlockData transferBlockData = mandomRotpAddon$handledPositions.remove(pos);
                if (transferBlockData == null) return;

                BlockState oldBlockState = transferBlockData.blockState;
                if (oldBlockState == null) return;

                MinecraftForge.EVENT_BUS.post(new SetBlockEvent(world, pos, oldBlockState, newState, transferBlockData));
            }
        }
    }
}
