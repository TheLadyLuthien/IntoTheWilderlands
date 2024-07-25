package com.skadoosh.wilderlands.blocks;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.block.dispenser.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockDestructionDispensorBehavior extends FallibleItemDispenserBehavior
{
	private static final Logger LOGGER = LogUtils.getLogger();

	@Override
	protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack)
	{
		this.setSuccess(false);
		Direction direction = pointer.state().get(DispenserBlock.FACING);
		BlockPos blockPos = pointer.pos().offset(direction);

		try
		{
			this.setSuccess(pointer.world().breakBlock(blockPos, true));
		}
		catch (Exception e)
		{
			LOGGER.error("Error trying to destroy block at {}", blockPos, e);
		}
		return null;
	}
}
