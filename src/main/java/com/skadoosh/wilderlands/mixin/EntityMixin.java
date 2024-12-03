package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.vehicle.VehicleEntity;

@Mixin(Entity.class)
public class EntityMixin
{
    @Inject(method = "canUsePortals", at = @At("HEAD"), cancellable = true)
    public void canUsePortals(boolean allowVehicles, CallbackInfoReturnable<Boolean> cir)
    {
        Object t = (Object)this;
        if (t instanceof EnderPearlEntity || t instanceof PlayerEntity || t instanceof VehicleEntity)
        {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }
    }
}
