package uk.meow.weever.rotp_mandom.data.entity;

import uk.meow.weever.rotp_mandom.data.global.LookData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Set;

public class ProjectileData {
    private final Vector3d position;
    private final Set<String> tags;
    private final boolean noPhysics;
    private final LookData lookData;
    private final LivingEntity owner;
    private final Vector3d deltaMovement;
    public ProjectileEntity entity;
    public boolean restored;

    public ProjectileData(ProjectileEntity entity, Vector3d position, Set<String> tags, boolean noPhysics, LookData lookData, LivingEntity owner, Vector3d deltaMovement, boolean restored) {
        this.entity = entity;
        this.position = position;
        this.tags = tags;
        this.noPhysics = noPhysics;
        this.lookData = lookData;
        this.owner = owner;
        this.deltaMovement = deltaMovement;
        this.restored = restored;
    }

    public void rewindProjectileData(ProjectileData projectileData) {
        ProjectileEntity entity = projectileData.entity;
        entity.moveTo(
                projectileData.position.x(),
                projectileData.position.y(),
                projectileData.position.z(),
                projectileData.lookData.lookVecX,
                projectileData.lookData.lookVecY
        );
        entity.setDeltaMovement(projectileData.deltaMovement);
        entity.noPhysics = projectileData.noPhysics;
        entity.setOwner(projectileData.owner);
        entity.getTags().clear();
        entity.getTags().addAll(projectileData.tags);
        projectileData.restored = true;
    }

    public static ProjectileData saveProjectileData(ProjectileEntity entity) {
        return new ProjectileData(
                entity,
                entity.position(),
                entity.getTags(),
                entity.noPhysics,
                new LookData(entity.xRot, entity.yRot),
                (LivingEntity) entity.getOwner(),
                entity.getDeltaMovement(),
                false
        );
    }

    public void rewindDeadProjectileEntityData(ProjectileData projectileData, World level) {
        if (!(projectileData.entity instanceof ProjectileEntity) || projectileData.restored) return;
    
        EntityType<?> entityType = projectileData.entity.getType();
        ProjectileEntity newEntity = (ProjectileEntity) entityType.create(level);
        if (newEntity == null) {
            return;
        }
        
        projectileData.entity = newEntity;
        level.addFreshEntity(newEntity);
        rewindProjectileData(projectileData);
    }
}
