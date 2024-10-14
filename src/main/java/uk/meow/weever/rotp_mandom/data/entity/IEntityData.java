package uk.meow.weever.rotp_mandom.data.entity;

import java.util.Queue;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IEntityData<T, M> {
    void rewindData(IEntityData<T, M> data);
    void rewindDeadData(IEntityData<T, M> data, World level);
    boolean isRestored(IEntityData<T, M> data);
    Entity getEntity(IEntityData<T, M> data);
    boolean inData(Queue<IEntityData<T, M>> data, M entity);
}