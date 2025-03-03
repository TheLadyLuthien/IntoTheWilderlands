package com.skadoosh.wilderlands.blocks;

import com.mojang.serialization.MapCodec;
import com.skadoosh.wilderlands.blockentities.CarvedRunestoneBlockEntity;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CarvedRunestoneBlock extends BlockWithEntity
{
    public static final EnumProperty<Rotation> ROTATION = EnumProperty.of("runestone_rotation", Rotation.class);
    public static final BooleanProperty GLOWING = BooleanProperty.of("runestone_glowing");
    public static final int GLOWING_LIGHT_LEVEL = 4;

    public static final VoxelShape TALL_SHAPE = Block.createCuboidShape(1, 1, 1, 15, 32, 15);

    public CarvedRunestoneBlock(Settings settings)
    {
        super(settings);
        setDefaultState(getDefaultState().with(ROTATION, Rotation.X).with(GLOWING, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(ROTATION);
        builder.add(GLOWING);
    }

    public static enum Rotation implements StringIdentifiable {
        X("x"), XY("xy"), Y("y"), YX("yx");

        private final String name;

        Rotation(final String name)
        {
            this.name = name;
        }

        @Override
        public String asString()
        {
            return name;
        }
    }

    public static int getLuminance(BlockState currentBlockState)
    {
        boolean activated = currentBlockState.get(GLOWING);
        return activated ? GLOWING_LIGHT_LEVEL : 1;
    }

	// @Override
	// protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
	// 	Vec3d vec3d = state.getModelOffset(world, pos);
	// 	return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
	// }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
    {
        return new CarvedRunestoneBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity entity, BlockHitResult hitResult)
    {
        if (!world.isClient)
        {
            if (state.get(GLOWING))
            {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof CarvedRunestoneBlockEntity)
                {
                    CarvedRunestoneBlockEntity runestone = (CarvedRunestoneBlockEntity)blockEntity;
                    RunicKeystoneBlock.trigger(world, runestone.getKeystonePos(), runestone);
    
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        return TALL_SHAPE;    
    }
    
    @Override
    protected boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos)
    {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockWithEntity.checkType(type, ModBlockEntities.CARVED_RUNESTONE_BLOCK_ENTITY, CarvedRunestoneBlockEntity::tick);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec()
    {
        return AbstractBlock.createCodec(CarvedRunestoneBlock::new);
    }

    // @SuppressWarnings("unchecked")
    // protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
    //     return expectedType == givenType ? (BlockEntityTicker<A>)ticker : null;
    // }

    // @Override
    // protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos)
    // {
    //     return TALL_SHAPE;
    // }

}
