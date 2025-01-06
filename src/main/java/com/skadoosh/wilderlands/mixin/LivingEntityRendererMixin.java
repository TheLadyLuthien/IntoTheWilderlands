package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin
{
    @Inject(method = "hasLabel", at = @At("HEAD"), cancellable = true)
    protected void hasLabel(@Coerce LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir)
    {
        var enchant = livingEntity.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.MASKING);
        if (EnchantmentHelper.getHighestEquippedLevel(enchant, livingEntity) > 0)
        {
            cir.setReturnValue(false);
            cir.cancel();
            return;
        }
    }
}
