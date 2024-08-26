package com.skadoosh.minigame.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class GravestoneBlock extends Block implements BlockEntityProvider
{
    public GravestoneBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos arg0, BlockState arg1)
    {
    }

}
