package uk.meow.weever.rotp_mandom.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import uk.meow.weever.rotp_mandom.event.custom.RemoveEntityEvent;

@Mixin(value=Entity.class, remap=false)
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
