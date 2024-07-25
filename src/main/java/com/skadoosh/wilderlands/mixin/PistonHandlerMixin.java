package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.piston.PistonHandler;

@Mixin(PistonHandler.class)
public class PistonHandlerMixin
{
    @ModifyConstant(method = "tryMove", constant = @Constant(intValue = 12), allow = 3)
    private int pistonPushLimit(int value)
    {
        return 64;
    }
}
