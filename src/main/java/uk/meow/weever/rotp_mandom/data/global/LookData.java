package uk.meow.weever.rotp_mandom.data.global;

import net.minecraft.nbt.CompoundNBT;

public class LookData {
    public final float lookVecX;
    public final float lookVecY;

    public LookData(float lookVecX, float lookVecY) {
        this.lookVecX = lookVecX;
        this.lookVecY = lookVecY;
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("LookVecX", this.lookVecX);
        nbt.putFloat("LookVecY", this.lookVecY);
        return nbt;
    }

    public static LookData fromNbt(CompoundNBT nbt) {
        float lookVecX = nbt.getFloat("LookVecX");
        float lookVecY = nbt.getFloat("LookVecY");
        return new LookData(lookVecX, lookVecY);
    }
}