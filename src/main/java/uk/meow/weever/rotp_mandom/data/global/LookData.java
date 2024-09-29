package uk.meow.weever.rotp_mandom.data.global;

public class LookData {
    private final float lookVecX;
    private final float lookVecY;

    public LookData(float lookVecX, float lookVecY) {
        this.lookVecX = lookVecX;
        this.lookVecY = lookVecY;
    }

    public float getLookVecX() {
        return lookVecX;
    }

    public float getLookVecY() {
        return lookVecY;
    }
}