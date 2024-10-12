package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.persistance.DashComponent;
import com.skadoosh.wilderlands.persistance.LiftComponent;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    // @Inject(method = "handleFrictionAndCalculateMovement", at = @At("HEAD"))
    // private void handleFrictionAndCalculateMovement(Vec3d movementInput, float slipperiness,
    // CallbackInfo ci)
    // {
    // LivingEntity e = ((LivingEntity)((Object)this));
    // int level =
    // EnchantmentHelper.getHighestEquippedLevel(e.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.SLIDE),
    // e);
    // float slip = ((level > 0) && e.isSneaking()) ? 1 : slipperiness;

    // // origional.call(movementInput, ((level > 0) && e.isSneaking()) ? 1 : slipperiness);
    // }
    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    private void modifyFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> ci)
    {
        LiftComponent liftComponent = ModComponentKeys.LIFT.getNullable(this);
        if (liftComponent != null && liftComponent.hasEnchantment())
        {
            if (liftComponent.isFalldamageImmune())
            {
                ci.setReturnValue(0);
                ci.cancel();
                return;
            }
        }
        
        DashComponent dashComponent = ModComponentKeys.DASH.getNullable(this);
        if (dashComponent != null && dashComponent.hasEnchantment())
        {
            if (dashComponent.isFalldamageImmune())
            {
                ci.setReturnValue(0);
                ci.cancel();
                return;
            }
        }
    }
}
