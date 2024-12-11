package uk.meow.weever.rotp_mandom.event.custom;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

public class RemoveEntityEvent extends EntityEvent {
    public RemoveEntityEvent(Entity entity) {
        super(entity);
    }
}
