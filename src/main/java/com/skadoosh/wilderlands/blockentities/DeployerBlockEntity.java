package com.skadoosh.wilderlands.blockentities;

import net.minecraft.block.entity.DispenserBlockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DeployerBlockEntity extends DispenserBlockEntity
{
    public DeployerBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.DEPLOYER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected Text getContainerName()
    {
        return Text.translatable("container.deployer");
    }
}
