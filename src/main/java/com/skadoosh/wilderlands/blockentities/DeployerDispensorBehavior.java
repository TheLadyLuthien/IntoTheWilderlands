// package com.skadoosh.wilderlands.blockentities;

// import net.minecraft.block.dispenser.DispenserBehavior;
// import net.minecraft.block.dispenser.DispenserBlock;
// import net.minecraft.entity.ItemEntity;
// import net.minecraft.item.BlockItem;
// import net.minecraft.item.ItemStack;
// import net.minecraft.util.math.BlockPointer;
// import net.minecraft.util.math.BlockPos;
// import net.minecraft.util.math.Direction;
// import net.minecraft.util.math.Position;
// import net.minecraft.world.World;
// import net.minecraft.world.WorldEvents;

// public class DeployerDispensorBehavior implements DispenserBehavior
// {
// 	@Override
// 	public final ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack)
// 	{
// 		ItemStack itemStack2 = this.dispenseSilently(blockPointer, itemStack);
// 		this.playSound(blockPointer);
// 		this.spawnParticles(blockPointer, blockPointer.state().get(DispenserBlock.FACING));
// 		return itemStack2;
// 	}

// 	protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack)
// 	{
// 		Direction direction = pointer.state().get(DispenserBlock.FACING);
// 		BlockPos pos = pointer.pos().offset(direction);

// 		ItemStack itemStack = stack.split(1);

// 		if (itemStack.getItem() instanceof BlockItem)
// 		{
// 			((BlockItem)(itemStack.getItem())).getBlock().getDefa
// 		}
		
// 		return stack;
// 	}

// 	public static void spawnItem(World world, ItemStack stack, int offset, Direction side, Position pos)
// 	{
// 		double d = pos.getX();
// 		double e = pos.getY();
// 		double f = pos.getZ();
// 		if (side.getAxis() == Direction.Axis.Y)
// 		{
// 			e -= 0.125;
// 		}
// 		else
// 		{
// 			e -= 0.15625;
// 		}

// 		ItemEntity itemEntity = new ItemEntity(world, d, e, f, stack);
// 		double g = world.random.nextDouble() * 0.1 + 0.2;
// 		itemEntity.setVelocity(world.random.nextTriangular((double)side.getOffsetX() * g, 0.0172275 * (double)offset), world.random.nextTriangular(0.2, 0.0172275 * (double)offset),
// 				world.random.nextTriangular((double)side.getOffsetZ() * g, 0.0172275 * (double)offset));
// 		world.spawnEntity(itemEntity);
// 	}

// 	protected void playSound(BlockPointer pointer)
// 	{
// 		playDefaultSound(pointer);
// 	}

// 	protected void spawnParticles(BlockPointer pointer, Direction side)
// 	{
// 		method_60580(pointer, side);
// 	}

// 	private static void playDefaultSound(BlockPointer pointer)
// 	{
// 		pointer.world().syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, pointer.pos(), 0);
// 	}

// 	private static void method_60580(BlockPointer pointer, Direction direction)
// 	{
// 		pointer.world().syncWorldEvent(WorldEvents.DISPENSER_FIRED, pointer.pos(), direction.getId());
// 	}

// 	protected ItemStack consumeWithRemainder(BlockPointer pointer, ItemStack stack, ItemStack remainder)
// 	{
// 		stack.decrement(1);
// 		if (stack.isEmpty())
// 		{
// 			return remainder;
// 		}
// 		else
// 		{
// 			this.method_60579(pointer, remainder);
// 			return stack;
// 		}
// 	}

// 	private void method_60579(BlockPointer pointer, ItemStack stack)
// 	{
// 		ItemStack itemStack = pointer.blockEntity().method_11075(stack);
// 		if (!itemStack.isEmpty())
// 		{
// 			Direction direction = pointer.state().get(DispenserBlock.FACING);
// 			spawnItem(pointer.world(), itemStack, 6, direction, DispenserBlock.method_58682(pointer));
// 			playDefaultSound(pointer);
// 			method_60580(pointer, direction);
// 		}
// 	}
// }
