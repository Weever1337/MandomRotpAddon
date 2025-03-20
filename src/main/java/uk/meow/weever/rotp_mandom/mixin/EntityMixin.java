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
}
