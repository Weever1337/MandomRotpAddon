package uk.meow.weever.rotp_mandom.mixin;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.meow.weever.rotp_mandom.event.custom.RemoveEntityEvent;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "remove(Z)V", at = @At("TAIL"))
    public void remove(boolean keepData, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RemoveEntityEvent((Entity) (Object) this));
    }
}
