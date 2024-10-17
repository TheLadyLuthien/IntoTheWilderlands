package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.RegistryKeys;

@Mixin(MobEntity.class)
public class MobEntityMixin
{
    @WrapOperation(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    public void tryAttack(LivingEntity target, double strength, double b, double c, Operation<Void> origional)
    {
        if (EnchantmentHelper.getHighestEquippedLevel(target.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.STONESPINED), target) > 0)
        {
            ((MobEntity)((Object)this)).takeKnockback(strength * 1.5, -b, -c);
        }
        else
        {
            origional.call(target, strength, b, c);
        }
    }
}
