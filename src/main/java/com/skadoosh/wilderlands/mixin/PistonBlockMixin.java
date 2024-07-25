package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(PistonBlock.class)
public class PistonBlockMixin
{
    @Inject(method = "isMovable", cancellable = true, at = @At("TAIL"))
    private static void isMovable(BlockState state, World world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonFacing, CallbackInfoReturnable<Boolean> ci)
    {
        ci.setReturnValue(!state.hasBlockEntity() || state.isOf(ModBlocks.DESTROYER) || state.isOf(ModBlocks.DEPLOYER));
    }
}
