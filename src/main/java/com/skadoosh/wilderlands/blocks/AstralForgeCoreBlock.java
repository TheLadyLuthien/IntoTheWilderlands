package com.skadoosh.wilderlands.blocks;

import com.mojang.serialization.MapCodec;
import com.skadoosh.wilderlands.blockentities.AstralForgeCoreBlockEntity;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AstralForgeCoreBlock extends BlockWithEntity
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
            if (blockEntity instanceof AstralForgeCoreBlockEntity afcBlockEntity)
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

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type)
    {
        return BlockWithEntity.checkType(type, ModBlockEntities.ASTRAL_FORGE_CORE_BLCOK_ENTITY, AstralForgeCoreBlockEntity::tick);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec()
    {
        return AbstractBlock.createCodec(AstralForgeCoreBlock::new);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.ANIMATED;
    }

    private static final VoxelShape BASE_CORE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);
    private static final VoxelShape BASE_CORE_COLLISION = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 19.0, 14.0);

    @Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return BASE_CORE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return BASE_CORE_COLLISION;
    }
}
