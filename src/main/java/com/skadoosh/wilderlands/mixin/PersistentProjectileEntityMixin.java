package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.projectile.PersistentProjectileEntity;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin
{
    @ModifyConstant(method = "applyKnockback", constant = @Constant(doubleValue = 0.0, ordinal = 0))
    private double injected(double value)
    {
        return -25000.0;
    }
}
