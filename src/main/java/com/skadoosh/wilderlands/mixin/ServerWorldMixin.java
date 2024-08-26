package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.base.Optional;
import com.skadoosh.minigame.ZoneHelper;
import com.skadoosh.minigame.ZoneHelper.ZoneType;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blockentities.CarvedRunestoneBlockEntity;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;
import com.skadoosh.wilderlands.blocks.CarvedRunestoneBlock;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.blocks.RunicKeystoneBlock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerWorld.class)
public class ServerWorldMixin
{
    @Inject(method = "canPlayerModifyAt", at = @At("HEAD"), cancellable = true)
    public void canPlayerModifyAt(PlayerEntity player, BlockPos origin, CallbackInfoReturnable<Boolean> ci)
    {
        if (!ZoneHelper.getZoneType(player).canBuild)
        {
            ci.setReturnValue(false);
            ci.cancel();
            return;
        }

        if (player.hasPermissionLevel(1))
        {
            return;
        }

        // not an OP, so check if within keystone bounds
        for (int x = -RunicKeystoneBlock.SEARCH_SIZE; x <= RunicKeystoneBlock.SEARCH_SIZE; x++)
        {
            for (int y = -RunicKeystoneBlock.SEARCH_SIZE; y <= RunicKeystoneBlock.SEARCH_SIZE; y++)
            {
                for (int z = -RunicKeystoneBlock.SEARCH_SIZE; z <= RunicKeystoneBlock.SEARCH_SIZE; z++)
                {
                    BlockPos pos = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    if (((ServerWorld)(Object)this).getBlockState(pos).isOf(ModBlocks.RUNIC_KEYSTONE))
                    {
                        ci.setReturnValue(false);
                        ci.cancel();
                    }
                }
            }
        }
    }
}
