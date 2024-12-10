package uk.meow.weever.rotp_mandom.capability.entity;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;

import java.util.LinkedList;

public class ProjectileEntityUtilCap {
    private final ProjectileEntity projectileEntity;
    private LinkedList<ProjectileData> projectileData = new LinkedList<>();

    public ProjectileEntityUtilCap(ProjectileEntity projectileEntity) {
        this.projectileEntity = projectileEntity;
    }

    public void tick() {
        int maxSize = RewindConfig.getSecond(projectileEntity.level.isClientSide());

        if (projectileEntity.tickCount % 20 == 0) {
            if (this.projectileData.size() > maxSize) {
                this.projectileData.removeFirst();
            }
            addProjectileData(ProjectileData.saveProjectileData(projectileEntity), maxSize);
        }
    }

    public LinkedList<ProjectileData> getProjectileData() {
        return projectileData;
    }

    public void addProjectileData(ProjectileData projectileData, int maxSize) {
        if (this.projectileData.size() > maxSize) {
            this.projectileData.removeFirst();
        }
        this.projectileData.addLast(projectileData);
    }

    public CompoundNBT toNBT() { // TODO: Save old info to NBT.
        return new CompoundNBT();
    }

    public void fromNBT(CompoundNBT ignored) {
    }
}
