package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Mixin(ElytraItem.class)
public class ElytraItemMixin
{
    @Inject(method = "isUsable", at = @At("HEAD"), cancellable = true)
    private static void isUsable(ItemStack stack, CallbackInfoReturnable<Boolean> ci)
    {
        ci.setReturnValue(stack.getDamage() < stack.getMaxDamage());
    }
    // @ModifyConstant(constant = @Constant(intValue = 432))
    // public int elytraDurability(int origional)
    // {
    // return 5;
    // }

    // @ModifyVariable(method = "maxDamage", at = @At("HEAD"))
    // private int injected(int y)
    // {
    // return 5;
    // }
}
