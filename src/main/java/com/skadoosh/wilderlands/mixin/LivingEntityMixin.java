package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.damage.ModDamageTypes;
import com.skadoosh.wilderlands.effects.ModEffects;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.persistance.DashComponent;
import com.skadoosh.wilderlands.persistance.LiftComponent;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.HolderLookup.RegistryLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    @WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    private float removeFriction(Block block, Operation<Float> origional)
    {
        LivingEntity e = ((LivingEntity)((Object)this));
        int level = EnchantmentHelper.getHighestEquippedLevel(e.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.SLIDE), e);

        float f = origional.call(block);
        return (e.isSneaking() && level > 0) ? 1f : f;
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

    @WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isTypeIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 6))
    private boolean takeKnockback(DamageSource source, TagKey<DamageType> tagKey, Operation<Boolean> origional)
    {
        LivingEntity e = ((LivingEntity)((Object)this));
        if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity attacker
                && EnchantmentHelper.getHighestEquippedLevel(e.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.STONESPINED), e) > 0)
        {
            double d = 0.0;
            double g = 0.0;
            Entity var13 = source.getSource();
            if (var13 instanceof ProjectileEntity)
            {
                ProjectileEntity projectileEntity = (ProjectileEntity)var13;
                DoubleDoubleImmutablePair doubleDoubleImmutablePair = projectileEntity.getHorizontalKnockback(e, source);
                d = -doubleDoubleImmutablePair.leftDouble();
                g = -doubleDoubleImmutablePair.rightDouble();
            }
            else if (source.getPosition() != null)
            {
                d = source.getPosition().getX() - e.getX();
                g = source.getPosition().getZ() - e.getZ();
            }

            attacker.takeKnockback(0.4000000059604645, -d, -g);
            return origional.call(source, tagKey) || true;
        }
        else
        {
            return origional.call(source, tagKey);
        }
    }

    @ModifyReturnValue(method = "getAttackDistanceScalingFactor", at = @At("RETURN"))
    private double modifyDetectionRange(double original, Entity entity)
    {
        if (entity == null && entity instanceof LivingEntity livingEntity/* || !entity.getType().isIn(ModEntityTypeTags.VEIL_IMMUNE) */)
        {
            var enchantment = livingEntity.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.VEIL);
            return (EnchantmentHelper.getHighestEquippedLevel(enchantment, livingEntity) > 0) ? original * 0.5 : original;
            // EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MODIFY_DETECTION_RANGE,
            // (LivingEntity)(Object)this, (float)original);
        }
        return original;
    }


    @Inject(method = "damage", at = @At("HEAD"))
    protected void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci)
    {
        LivingEntity e = ((LivingEntity)((Object)this));
        if (!e.isInvulnerableTo(source) && !source.isType(ModDamageTypes.BLEEDING) && !e.getWorld().isClient)
        {
            if (e.hasStatusEffect(ModEffects.BLEEDING))
            {
                var effect = e.getStatusEffect(ModEffects.BLEEDING);
                e.setStatusEffect(new StatusEffectInstance(ModEffects.BLEEDING, 20 * 5, effect.getAmplifier()), source.getSource());
            }
        }
    }
}
