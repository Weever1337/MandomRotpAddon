package uk.meow.weever.rotp_mandom.data.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.config.TPARConfig;

public class EntityData {
    public Entity entity;
    private final Vector3d position;
    public boolean restored;

    public EntityData(Entity entity, Vector3d position, boolean restored) {
        this.entity = entity;
        this.position = position;
        this.restored = restored;
    }

    public static void rewindEntityData(EntityData entityData) {
        Entity entity = entityData.entity;
        entity.moveTo(entityData.position);
        entityData.restored = true;
    }

    public static EntityData saveEntityData(Entity entity) {
        return new EntityData(
                entity,
                entity.position(),
                false
        );
    }

    public static void rewindDeadEntityData(EntityData entityData, World level) {
        if (!TPARConfig.getRewindDeadEntities(level.isClientSide())) return;
        EntityType<?> entityType = entityData.entity.getType();
        Entity newEntity = entityType.create(level);
        if (newEntity == null) return;
        entityData.entity = newEntity;
        level.addFreshEntity(newEntity);
        rewindEntityData(entityData);
    }
}
