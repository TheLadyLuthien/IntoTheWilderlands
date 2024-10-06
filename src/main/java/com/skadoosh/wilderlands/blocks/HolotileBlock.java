package com.skadoosh.wilderlands.blocks;

import java.rmi.registry.Registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class HolotileBlock extends GlassBlock
{
    public HolotileBlock(Settings settings)
    {
        super(settings);
        setDefaultState(getDefaultState().with(OPEN, true).with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(Properties.OPEN);
        builder.add(Properties.POWERED);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random)
    {
        flipPos(world, pos);
        flipNeighbors(world, pos);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos)
    {
        return state.get(OPEN);
    }

    private void flipPos(ServerWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(OPEN, !state.get(OPEN)));
    }

    private void flipNeighbors(ServerWorld world, BlockPos pos)
    {
        for (Direction direction : Direction.values())
        {
            BlockPos nextPos = pos.offset(direction);
            if (world.getBlockState(nextPos).getBlock() instanceof HolotileBlock)
            {
                if (world.getBlockState(nextPos).get(OPEN).booleanValue() != world.getBlockState(pos).get(OPEN))
                {
                    world.scheduleBlockTick(nextPos, this, 2);
                }
            }
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify)
    {
        if (world instanceof ServerWorld serverWorld)
        {
            boolean bl = world.isReceivingRedstonePower(pos);
            if (bl != (Boolean)state.get(POWERED))
            {
                BlockState blockState = state;
                if (!(Boolean)state.get(POWERED))
                {
                    blockState = (BlockState)state.cycle(OPEN);
                    world.playSound((PlayerEntity)null, pos, (Boolean)blockState.get(OPEN) ? SoundEvents.BLOCK_COPPER_BULB_TURN_ON : SoundEvents.BLOCK_COPPER_BULB_TURN_OFF, SoundCategory.BLOCKS);
                    
                    flipPos(serverWorld, pos);
                    flipNeighbors(serverWorld, pos);
                }

                world.setBlockState(pos, (BlockState)blockState.with(POWERED, bl), Block.NOTIFY_ALL);
                return;
            }
        }
    }

    public void setState(BlockState state, ServerWorld world, BlockPos pos)
    {
        boolean bl = world.isReceivingRedstonePower(pos);
        if (bl != (Boolean)state.get(POWERED))
        {
            BlockState blockState = state;
            if (!(Boolean)state.get(POWERED))
            {
                blockState = (BlockState)state.cycle(OPEN);
                world.playSound((PlayerEntity)null, pos, (Boolean)blockState.get(OPEN) ? SoundEvents.BLOCK_COPPER_BULB_TURN_ON : SoundEvents.BLOCK_COPPER_BULB_TURN_OFF, SoundCategory.BLOCKS);
            }

            world.setBlockState(pos, (BlockState)blockState.with(POWERED, bl), Block.NOTIFY_ALL);
        }
    }

    public static final BooleanProperty POWERED;
    public static final BooleanProperty OPEN;
    static
    {
        POWERED = Properties.POWERED;
        OPEN = Properties.OPEN;
    }
}
