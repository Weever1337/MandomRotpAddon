package uk.meow.weever.rotp_mandom.capability.entity;

import com.github.standobyte.jojo.capability.world.TimeStopHandler;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.meow.weever.rotp_mandom.config.RewindConfig;
import uk.meow.weever.rotp_mandom.data.entity.ProjectileData;
import uk.meow.weever.rotp_mandom.util.AddonUtil;

import java.util.LinkedList;

public class ProjectileEntityUtilCap {
    private final ProjectileEntity projectileEntity;
    private final LinkedList<ProjectileData> projectileData = new LinkedList<>();
    private int capabilitySeconds = 0;

    public ProjectileEntityUtilCap(ProjectileEntity projectileEntity) {
        this.projectileEntity = projectileEntity;
    }

    public void tick() {
        int maxSize = RewindConfig.getSecond(projectileEntity.level.isClientSide());

        if (projectileEntity.tickCount % 20 == 0) {
            capabilitySeconds++;
            if (this.projectileData.size() > maxSize) {
                this.projectileData.removeFirst();
            }
            if (!TimeStopHandler.isTimeStopped(projectileEntity.level, projectileEntity.blockPosition())) {
                addProjectileData(ProjectileData.saveProjectileData(projectileEntity), maxSize);
            }
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

    public int getCapabilitySeconds() {
        return capabilitySeconds;
    }

    public CompoundNBT toNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("capabilitySeconds", capabilitySeconds);
        return nbt;
    }

    public void fromNBT(CompoundNBT nbt) {
        capabilitySeconds = nbt.getInt("capabilitySeconds");
    }
}
