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

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.HolderLookup.RegistryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    @WrapOperation(method = "travel", at = @At(value="INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    private float removeFriction(Block block, Operation<Float> origional)
    {
        LivingEntity e = ((LivingEntity)((Object)this));
        int level = EnchantmentHelper.getHighestEquippedLevel(e.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.SLIDE), e);

        float f = origional.call(block);
        return (e.isSneaking() && level > 0) ? 0.99999999999f : f;
    }

    @Inject(method = "hasNoDrag", at = @At("HEAD"), cancellable = true)
    private void hasNoDrag(CallbackInfoReturnable<Boolean> ci)
    {
        LivingEntity e = ((LivingEntity)((Object)this));
        if (e.isSneaking() && e.isOnGround())
        {
            int level = EnchantmentHelper.getHighestEquippedLevel(e.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.SLIDE), e);
            if (level > 0)
            {
                ci.setReturnValue(true);
                ci.cancel();
                return;
            }
        }
    }

    @WrapOperation(method = "handleFrictionAndCalculateMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getMovementSpeed(F)F"))
    private float injected(LivingEntity e, float slipperiness, Operation<Float> origional)
    {
        RegistryLookup<Enchantment> lookupOrThrow = e.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT);
        int adrenaline = EnchantmentHelper.getHighestEquippedLevel(lookupOrThrow.getHolderOrThrow(ModEnchantments.ADRENALINE), e);

        float speed = origional.call(e, slipperiness);

        if (adrenaline > 0)
        {
            float boost = (1 - (e.getHealth() / e.getMaxHealth())) * ModEnchantments.ADRENALINE_MAX_SPEED_BOOST;
            speed += boost;
        }

        return speed;
    }

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
