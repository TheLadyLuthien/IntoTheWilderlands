package com.skadoosh.wilderlands.blocks;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blockentities.DeployerBlockEntity;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;

public class DeployerBlock extends DispenserBlock
{
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final MapCodec<DeployerBlock> CODEC = createCodec(DeployerBlock::new);
	private static final DispenserBehavior BEHAVIOR = new BlockPlacementDispenserBehavior();
	private static final DispenserBehavior FAIL_BEHAVIOR = new ItemDispenserBehavior();

	@Override
	public MapCodec<DeployerBlock> getCodec()
	{
		return CODEC;
	}

	public DeployerBlock(AbstractBlock.Settings settings)
	{
		super(settings);
	}

	@Override
	protected DispenserBehavior getBehaviorForItem(World world, ItemStack stack)
	{
		if (stack.getItem() instanceof BlockItem)
		{
			Wilderlands.LOGGER.info("Placment Behavior");
			return BEHAVIOR;
		}
		else
		{
			Wilderlands.LOGGER.info("Drop item Behavior");
			return FAIL_BEHAVIOR;
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new DeployerBlockEntity(pos, state);
	}

	@Override
	protected void dispense(ServerWorld world, BlockState state, BlockPos pos)
	{
		DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity)world.getBlockEntity(pos, ModBlockEntities.DEPLOYER_BLOCK_ENTITY).orElse(null);
		if (dispenserBlockEntity == null)
		{
			LOGGER.warn("Ignoring dispensing attempt for Deployer without matching block entity at {}", pos);
		}
		else
		{
			BlockPointer blockPointer = new BlockPointer(world, pos, state, dispenserBlockEntity);
			int i = dispenserBlockEntity.chooseNonEmptySlot(world.random);
			if (i < 0)
			{
				world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, pos, 0);
			}
			else
			{
				ItemStack itemStack = dispenserBlockEntity.getStack(i);
				if (!itemStack.isEmpty())
				{
					ItemStack itemStack2 = getBehaviorForItem(world, itemStack).dispense(blockPointer, itemStack);
					dispenserBlockEntity.setStack(i, itemStack2);
				}
			}
		}
	}
}
