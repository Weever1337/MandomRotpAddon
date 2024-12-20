package uk.meow.weever.rotp_mandom.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.meow.weever.rotp_mandom.capability.entity.*;
import uk.meow.weever.rotp_mandom.event.custom.RemoveEntityEvent;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "remove(Z)V", at = @At("TAIL"))
    public void remove(boolean keepData, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RemoveEntityEvent((Entity) (Object) this));
    }

//    @Inject(method = "tick", at = @At("TAIL"))
//    public void tick(CallbackInfo ci) {
//        Entity entity = (Entity) (Object) this;
//        if (entity instanceof LivingEntity && !(entity instanceof ArmorStandEntity) && !((LivingEntity) entity).isDeadOrDying()) {
//            entity.getCapability(LivingEntityUtilCapProvider.CAPABILITY).ifPresent(LivingEntityUtilCap::tick);
//        } else if (entity instanceof ProjectileEntity && entity.isAlive()) {
//            entity.getCapability(ProjectileEntityUtilCapProvider.CAPABILITY).ifPresent(ProjectileEntityUtilCap::tick);
//        } else if (entity instanceof ItemEntity && entity.isAlive()) {
//            entity.getCapability(ItemEntityUtilCapProvider.CAPABILITY).ifPresent(ItemEntityUtilCap::tick);
//        }
//    }
}
