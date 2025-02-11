package com.skadoosh.wilderlands.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.TriggeredCrossbowBehavior;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
        CrossbowProjectileBehavior behavior = CrossbowProjectileBehavior.getBehavior(arrow.getItem());
        cir.setReturnValue(behavior.getProjectileEntity(world, entity, weapon, arrow, isCritical));
        cir.cancel();
        return;
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;shootAll(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;FFLnet/minecraft/entity/LivingEntity;)V"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, @Local LocalRef<ChargedProjectilesComponent> projectiles)
    {
        final ItemStack weapon = user.getStackInHand(hand);

        boolean hasTrigger = false;
        for (ItemStack arrow : projectiles.get().getProjectiles())
        {
            CrossbowProjectileBehavior behavior = CrossbowProjectileBehavior.getBehavior(arrow.getItem());
            if (behavior instanceof TriggeredCrossbowBehavior trigger)
            {
                trigger.onTrigger(world, user, weapon, arrow);
                weapon.damageEquipment(trigger.getUseDamage(), user, LivingEntity.getHand(hand));
                hasTrigger = true;
            }

            behavior.applyRecoil(user, weapon);
        } 

        if (hasTrigger)
        {
            weapon.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
            cir.setReturnValue(TypedActionResult.consume(weapon));
            cir.cancel();
            return;
        }
    }
}
