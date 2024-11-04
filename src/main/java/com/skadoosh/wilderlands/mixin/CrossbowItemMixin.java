package com.skadoosh.wilderlands.mixin;

import java.util.Comparator;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Comparators;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin
{
    @Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
    private static void getSpeed(ChargedProjectilesComponent component, CallbackInfoReturnable<Float> cir)
    {
        float speed = component.getProjectiles().stream().map(stack -> CrossbowProjectileBehavior.getBehavior(stack.getItem()).getLaunchSpeed()).sorted().findFirst().orElse(3.15f);
        cir.setReturnValue(speed);
        cir.cancel();
        return;
    }

    @Inject(method = "getUseDamage", at = @At("HEAD"), cancellable = true)
    protected void getUseDamage(ItemStack stack, CallbackInfoReturnable<Integer> cir)
    {
        cir.setReturnValue(CrossbowProjectileBehavior.getBehavior(stack.getItem()).getUseDamage());
        cir.cancel();
        return;
    }

    @WrapOperation(method = {"loadProjectiles", "use"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getArrowType(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack getArrowType(LivingEntity user, ItemStack weapon, Operation<ItemStack> original)
    {
        // dump the original value
        original.call(user, weapon);
        return CrossbowProjectileBehavior.getArrowItemStack(user, weapon);
    }

    @Inject(method = {"getHeldProjectiles", "getProjectiles"}, at = @At("HEAD"), cancellable = true)
    public void getProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cir)
    {
        cir.setReturnValue(CrossbowProjectileBehavior.isValidProjectile());
    }

    @Inject(method = "getProjectile", at = @At("HEAD"), cancellable = true)
    protected void getProjectile(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical, CallbackInfoReturnable<ProjectileEntity> cir)
    {
        cir.setReturnValue(CrossbowProjectileBehavior.getBehavior(arrow.getItem()).getProjectileEntity(world, entity, weapon, arrow, isCritical));
        cir.cancel();
        return;
    }
}
