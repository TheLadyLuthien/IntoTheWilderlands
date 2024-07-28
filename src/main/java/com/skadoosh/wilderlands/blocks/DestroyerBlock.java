package com.skadoosh.wilderlands.blocks;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.skadoosh.wilderlands.misc.BlockDestructionDispensorBehavior;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DestroyerBlock extends DispenserBlock
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<DestroyerBlock> CODEC = createCodec(DestroyerBlock::new);
    private static final DispenserBehavior BEHAVIOR = new BlockDestructionDispensorBehavior();

    @Override
    public MapCodec<DestroyerBlock> getCodec()
    {
        return CODEC;
    }

    public DestroyerBlock(AbstractBlock.Settings settings)
    {
        super(settings);
    }

    @Override
    protected DispenserBehavior getBehaviorForItem(World world, ItemStack stack)
    {
        return BEHAVIOR;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
    {
        return null;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity entity, BlockHitResult hitResult)
    {
        return ActionResult.PASS;
    }

    @Override
    protected void dispense(ServerWorld world, BlockState state, BlockPos pos)
    {
        BlockPointer blockPointer = new BlockPointer(world, pos, state, null);
        getBehaviorForItem(world, null).dispense(blockPointer, null);
    }
}
