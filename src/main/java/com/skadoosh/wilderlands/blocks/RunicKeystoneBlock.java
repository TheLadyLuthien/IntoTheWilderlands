package com.skadoosh.wilderlands.blocks;

import java.util.List;
import java.util.Optional;
import java.util.EnumSet;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blockentities.CarvedRunestoneBlockEntity;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.entities.BifrostBeamEntity;
import com.skadoosh.wilderlands.misc.BifrostHelper;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.NamedKeystoneData.KeystoneLocation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
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
            ItemStack keyStack = BifrostHelper.getKeyStack(entity);

            if (keyStack != null && keyStack.get(ModComponents.BIFROST_KEY) != null && !state.get(Properties.TRIGGERED).booleanValue())
            {
                if (enable(pos, (ServerWorld)world, keyStack))
                {
                    BifrostHelper.expendKeyIfNecessary(keyStack, entity);
                    return ActionResult.SUCCESS;
                }
                else
                {
                    return ActionResult.FAIL;
                }
            }
        }
        return ActionResult.FAIL;
    }

    public static final int SEARCH_SIZE = 5;

    private static boolean enable(BlockPos origin, ServerWorld world, ItemStack keyStack)
    {
        int enabledCount = 0;
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

                        if (BifrostHelper.shouldActivateRunestone(keyStack, world, origin, opt.get()))
                        {
                            enabledCount++;
                            BlockState blockState = world.getBlockState(pos);
                            world.setBlockState(pos, blockState.with(CarvedRunestoneBlock.GLOWING, true));
                        }
                    }
                }
            }
        }

        if (enabledCount > 0)
        {
            BlockState blockState = world.getBlockState(origin);
            world.setBlockState(origin, blockState.with(Properties.TRIGGERED, true));

            return true;
            // playEnableParticles(world, origin);
        }
        else
        {
            return false;
        }
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

    public static void trigger(World originWorld, BlockPos originKeystonePos, CarvedRunestoneBlockEntity blockEntity)
    {
        Identifier destinationWorldidentifier = Identifier.parse(blockEntity.getDestinationDimension());
        RegistryKey<World> destinationWorldRegistryKey = RegistryKey.of(RegistryKeys.WORLD, destinationWorldidentifier);

        if (!originWorld.isClient)
        {
            ServerWorld destinationWorld = originWorld.getServer().getWorld(destinationWorldRegistryKey);
            if (destinationWorld == null)
            {
                Wilderlands.LOGGER.error("Carved runestone couldn't find target dimension");
            }
            else
            {
                // Chunk chunk = destinationWorld.getChunk(blockEntity.getDestinationPos());
                // if (destinationWorld.isChunkLoaded(chunk.getPos().x, chunk.getPos().z))
                // {
                //     BifrostBeamEntity bifrostBeamEntity = new BifrostBeamEntity(destinationWorld, BifrostBeamEntity.DEFAULT_DURATION, new KeystoneLocation(originWorld.getRegistryKey().getValue(), originKeystonePos));
                //     bifrostBeamEntity.setPos(blockEntity.getDestinationPos().getX(), blockEntity.getDestinationPos().getY(), blockEntity.getDestinationPos().getZ());

                //     destinationWorld.spawnEntity(bifrostBeamEntity);
                //     // other end is loaded, play particles here too!
                // }

                // deactivate
                disable(originKeystonePos, originWorld);

                BifrostBeamEntity bifrostBeamEntity = new BifrostBeamEntity(originWorld, BifrostBeamEntity.DEFAULT_DURATION, new KeystoneLocation(destinationWorldidentifier, blockEntity.getDestinationPos()));
                bifrostBeamEntity.setPos(originKeystonePos.getX(), originKeystonePos.getY(), originKeystonePos.getZ());

                originWorld.spawnEntity(bifrostBeamEntity);
            }
        }
    }

    // private static void playEnableParticles(ServerWorld world, BlockPos pos)
    // {
    // final int count = 12;
    // final float speed = 1f;

    // final double angleIncrement = 2 * Math.PI / count;
    // for (int i = 0; i < count; i++)
    // {
    // double angle = (i * angleIncrement);
    // float x = (float)(0.5 * Math.cos(angle));
    // float z = (float)(0.5 * Math.sin(angle));

    // ModParticles.KEYSTONE_ACTIVATE.spawn(world, pos.getX() + x + 0.5f, pos.getY() + 1.1f, pos.getZ()
    // + z + 0.5f, x * speed, 0, z * speed);
    // }
    // }

}
