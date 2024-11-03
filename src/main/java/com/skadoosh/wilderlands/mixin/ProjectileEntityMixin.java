package com.skadoosh.wilderlands.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.RegistryKeys;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin
{
    @Shadow
    @Nullable
    public abstract Entity getOwner();

    @ModifyVariable(method = "setVelocity(DDDFF)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    public float removeDivergenceForPlayers(float divergence)
    {
        if (getOwner() instanceof PlayerEntity pe && (EnchantmentHelper.getLevel(pe.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.PINPOINT), pe.getMainHandStack()) > 0))
            return 0;
        return divergence;
    }
}
