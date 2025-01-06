package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void getNightVisionStrength(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir)
    {
        var enchantment = entity.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.ILLUMINTING);
        if (EnchantmentHelper.getHighestEquippedLevel(enchantment, entity) > 0)
        {
            cir.setReturnValue(1.0f);
            cir.cancel();
            return;
        }
    }
}
