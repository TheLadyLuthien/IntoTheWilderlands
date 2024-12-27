package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.registry.RegistryKeys;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin
{
    @Shadow
    private MinecraftClient client;

    @ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/registry/Holder;)Z", ordinal = 0))
    private boolean hasNightVision(boolean original)
    {
        return original || (EnchantmentHelper.getHighestEquippedLevel(client.player.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.ILLUMINTING), client.player) > 0);
    }
}
