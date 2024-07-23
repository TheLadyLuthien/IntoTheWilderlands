package com.skadoosh.wilderlands.blocks;

import java.util.Optional;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blockentities.CarvedRunestoneBlockEntity;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;
import com.skadoosh.wilderlands.components.ModComponents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RunicKeystoneBlock extends Block
{
    public RunicKeystoneBlock(Settings settings)
    {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.TRIGGERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(Properties.TRIGGERED);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity entity, BlockHitResult hitResult)
    {
        if (!world.isClient)
        {
            final ItemStack mainHand = entity.getStackInHand(Hand.MAIN_HAND);
            final ItemStack offhand = entity.getStackInHand(Hand.OFF_HAND);

            NbtComponent component = null;

            if (mainHand.contains(ModComponents.BIFROST_KEY))
            {
                component = mainHand.get(ModComponents.BIFROST_KEY);
            }
            else if (offhand.contains(ModComponents.BIFROST_KEY))
            {
                component = offhand.get(ModComponents.BIFROST_KEY);
            }

            if (component != null)
            {
                enable(pos, world);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    private static final int SEARCH_SIZE = 5;

    private static void enable(BlockPos origin, World world)
    {
        // loop through nearby blocks for runestones
        for (int x = -SEARCH_SIZE; x <= SEARCH_SIZE; x++)
        {
            for (int y = -SEARCH_SIZE; y <= SEARCH_SIZE; y++)
            {
                for (int z = -SEARCH_SIZE; z <= SEARCH_SIZE; z++)
                {
                    BlockPos pos = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    Optional<CarvedRunestoneBlockEntity> opt = world.getBlockEntity(pos, ModBlockEntities.CARVED_RUNESTONE_BLOCK_ENTITY);
                    
                    if (opt.isPresent())
                    {
                        opt.get().setKeystonePos(origin);

                        BlockState blockState = world.getBlockState(pos);
                        world.setBlockState(pos, blockState.with(CarvedRunestoneBlock.GLOWING, true));
                    }
                }
            }
        }
        
        BlockState blockState = world.getBlockState(origin);
        world.setBlockState(origin, blockState.with(Properties.TRIGGERED, true));
    }
    
    private static void disable(BlockPos origin, World world)
    {
        for (int x = -SEARCH_SIZE; x <= SEARCH_SIZE; x++)
        {
            for (int y = -SEARCH_SIZE; y <= SEARCH_SIZE; y++)
            {
                for (int z = -SEARCH_SIZE; z <= SEARCH_SIZE; z++)
                {
                    BlockPos pos = new BlockPos(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    Optional<CarvedRunestoneBlockEntity> opt = world.getBlockEntity(pos, ModBlockEntities.CARVED_RUNESTONE_BLOCK_ENTITY);
                    
                    if (opt.isPresent())
                    {
                        BlockState blockState = world.getBlockState(pos);
                        world.setBlockState(pos, blockState.with(CarvedRunestoneBlock.GLOWING, false));
                    }
                }
            }
        }

        BlockState blockState = world.getBlockState(origin);
        world.setBlockState(origin, blockState.with(Properties.TRIGGERED, false));
    }

    @SuppressWarnings("resource")
    public static void trigger(BlockPos origin, CarvedRunestoneBlockEntity blockEntity, ServerPlayerEntity user)
    {
        Identifier identifier = Identifier.parse(blockEntity.getDimension());
        RegistryKey<World> registryKey = RegistryKey.of(RegistryKeys.WORLD, identifier);

        if (!blockEntity.getWorld().isClient)
        {
            ServerWorld serverWorld = blockEntity.getWorld().getServer().getWorld(registryKey);
            if (serverWorld == null)
            {
                Wilderlands.LOGGER.error("Carved runestone couldn't find target dimension");
            }
            else
            {
                // TODO: play the particles & delay

                Chunk chunk = serverWorld.getChunk(blockEntity.getDestinationPos());
                if (serverWorld.isChunkLoaded(chunk.getPos().x, chunk.getPos().z))
                {
                    // other end is loaded, play particles here too!
                }

                // deactivate
                disable(origin, blockEntity.getWorld());
                
                // teleport the user (do this last)
                user.teleport(serverWorld, blockEntity.getDestinationPos().getX(), blockEntity.getDestinationPos().getY(), blockEntity.getDestinationPos().getZ(), user.getYaw(), user.getPitch());
            }
        }
    }
}
