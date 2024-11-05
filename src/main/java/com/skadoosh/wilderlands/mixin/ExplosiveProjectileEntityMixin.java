package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;

@Mixin(ExplosiveProjectileEntity.class)
public class ExplosiveProjectileEntityMixin
{
    @Inject(method = "canHit", at = @At("HEAD"), cancellable = true)
    protected void canHit(Entity entity, CallbackInfoReturnable<Boolean> cir)
    {
        if (entity instanceof ExplosiveProjectileEntity)
        {
            cir.setReturnValue(false);
            cir.cancel();
        }        
    }
}
