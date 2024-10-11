package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    // @Inject(method = "handleFrictionAndCalculateMovement", at = @At("HEAD"))
    // private void handleFrictionAndCalculateMovement(Vec3d movementInput, float slipperiness, CallbackInfo ci)
    // {
    //     LivingEntity e = ((LivingEntity)((Object)this));
    //     int level = EnchantmentHelper.getHighestEquippedLevel(e.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.SLIDE), e);
    //     float slip = ((level > 0) && e.isSneaking()) ? 1 : slipperiness;
        
    //     // origional.call(movementInput, ((level > 0) && e.isSneaking()) ? 1 : slipperiness);
    // }
}
