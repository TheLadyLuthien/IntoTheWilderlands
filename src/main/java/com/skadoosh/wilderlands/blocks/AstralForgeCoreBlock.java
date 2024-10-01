package com.skadoosh.wilderlands.blocks;

import com.skadoosh.wilderlands.blockentities.AstralForgeCoreBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AstralForgeCoreBlock extends Block implements BlockEntityProvider
{
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity entity, BlockHitResult hitResult)
    {
        if (world.isClient)
        {
            return ActionResult.SUCCESS;
        }
        else
        {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BeaconBlockEntity afcBlockEntity)
            {
                entity.openHandledScreen(afcBlockEntity);
            }

            return ActionResult.CONSUME;
        }
    }

    public AstralForgeCoreBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
    {
        return new AstralForgeCoreBlockEntity(pos, state);
    }
}
