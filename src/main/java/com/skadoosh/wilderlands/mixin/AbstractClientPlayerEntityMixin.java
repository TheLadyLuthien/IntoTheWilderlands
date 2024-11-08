package com.skadoosh.wilderlands.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.skadoosh.wilderlands.items.ModItems;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

@ClientOnly
@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin
{
    @Inject(method = "getSpeed", at = @At("HEAD"), cancellable = true)
	public void getSpeed(CallbackInfoReturnable<Float> cir)
    {
        AbstractClientPlayerEntity cpe = (AbstractClientPlayerEntity)(Object)(this);
        ItemStack stack = cpe.getStackInHand(Hand.MAIN_HAND);
        
        boolean isCrossbow = false;
        if (stack.isOf(Items.CROSSBOW))
        {
            isCrossbow = true;
        }
        else
        {
            stack = cpe.getStackInHand(Hand.OFF_HAND);
            if (stack.isOf(Items.CROSSBOW))
            {
                isCrossbow = true;
            }
        }

        if (isCrossbow)
        {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            if (chargedProjectilesComponent.contains(ModItems.BRIMSTONE_CARTRIDGE))
            {
                cir.setReturnValue(0.055f);
                cir.cancel();
                return;
            }
        }
    }
}
