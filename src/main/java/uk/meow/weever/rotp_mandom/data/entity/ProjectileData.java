package uk.meow.weever.rotp_mandom.data.entity;

import uk.meow.weever.rotp_mandom.config.TPARConfig;
import uk.meow.weever.rotp_mandom.data.global.LookData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProjectileData implements IEntityData<ProjectileData, ProjectileEntity> {
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

    public static void rewindProjectileData(ProjectileData projectileData) {
        ProjectileEntity entity = projectileData.entity;
        entity.moveTo(
                projectileData.position.x(),
                projectileData.position.y(),
                projectileData.position.z(),
                projectileData.lookData.getLookVecX(),
                projectileData.lookData.getLookVecY()
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

    public static void rewindDeadProjectileEntityData(ProjectileData projectileData, World level) {
        if (!TPARConfig.getRewindDeadLivingEntities(level.isClientSide()) || !(projectileData.entity instanceof ProjectileEntity)) return;
    
        EntityType<?> entityType = projectileData.entity.getType();
        if (entityType == null) {
            return;
        }
        ProjectileEntity newEntity = (ProjectileEntity) entityType.create(level);
        if (newEntity == null) {
            return;
        }
        
        projectileData.entity = newEntity;
        level.addFreshEntity(newEntity);
        rewindProjectileData(projectileData);
    }

    @Override
    public Entity getEntity(IEntityData<ProjectileData, ProjectileEntity> data) {
        return ((ProjectileData)data).entity;
    }

    @Override
    public void rewindData(IEntityData<ProjectileData, ProjectileEntity> data) {
        if (((ProjectileData)data).restored) return;
        rewindProjectileData(((ProjectileData)data));
    }

    @Override
    public boolean isRestored(IEntityData<ProjectileData, ProjectileEntity> data) {
        return ((ProjectileData)data).restored;
    }

    @Override
    public void rewindDeadData(IEntityData<ProjectileData, ProjectileEntity> data, World level) {
        if (((ProjectileData)data).restored) return;
        rewindDeadProjectileEntityData(((ProjectileData)data), level);
    }

    @Override
    public boolean inData(Queue<IEntityData<ProjectileData, ProjectileEntity>> projData, ProjectileEntity entity) {
        AtomicBoolean inData = new AtomicBoolean(false);
        projData.forEach(data -> {
            if (data.getEntity(data) == entity) {
                inData.set(true);
            }
        });
        return inData.get();
    }
}
