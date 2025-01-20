package com.skadoosh.wilderlands.blockentities;

import java.util.List;
import java.util.Optional;

import com.skadoosh.cadmium.animation.AnimationStep;
import com.skadoosh.cadmium.animation.ParticleAnimation;
import com.skadoosh.wilderlands.blocks.CarvedRunestoneBlock;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.misc.BifrostHelper;
import com.skadoosh.wilderlands.misc.ModParticles;

import foundry.veil.api.client.anim.Frame;
import foundry.veil.api.client.anim.Keyframe;
import foundry.veil.api.client.anim.Path;
import foundry.veil.api.client.util.Easings.Easing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.skadoosh.wilderlands.blocks.RunicKeystoneBlock.SEARCH_SIZE;

public class CarvedRunestoneBlockEntity extends BlockEntity
{
    private int animationTick = 0;
    private int ticks = 0;
    public int getAnimationTick()
    {
        return this.animationTick;
    }
    public void setAnimationTick(int t)
    {
        this.animationTick = t;
    }

    public CarvedRunestoneBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CARVED_RUNESTONE_BLOCK_ENTITY, pos, state);
    }

    private void searchAndAssignKeystone()
    {
        for (int x = -SEARCH_SIZE; x <= SEARCH_SIZE; x++)
        {
            for (int y = -SEARCH_SIZE; y <= SEARCH_SIZE; y++)
            {
                for (int z = -SEARCH_SIZE; z <= SEARCH_SIZE; z++)
                {
                    BlockPos testPos = new BlockPos(this.getPos().getX() + x, this.getPos().getY() + y, this.getPos().getZ() + z);

                    if (world.getBlockState(testPos).isOf(ModBlocks.RUNIC_KEYSTONE))
                    {
                        this.keystonePos = testPos;
                        return;
                    }
                }
            }
        }
    }

    private BlockPos destinationPos = new BlockPos(0, 100, 0);

    public BlockPos getDestinationPos()
    {
        return destinationPos;
    }

    private BlockPos keystonePos = new BlockPos(10, 90, 10);

    public BlockPos getKeystonePos()
    {
        return keystonePos;
    }

    private String dimension = "minecraft:overworld";

    public String getDestinationDimension()
    {
        return dimension;
    }

    public void setDestination(BlockPos pos, World world)
    {
        destinationPos = pos;
        dimension = world.getRegistryKey().getValue().toString();
        this.markDirty();
    }

    public void setKeystonePos(BlockPos pos)
    {
        keystonePos = pos;
        this.markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, Provider lookupProvider)
    {
        nbt.putInt("destX", destinationPos.getX());
        nbt.putInt("destY", destinationPos.getY());
        nbt.putInt("destZ", destinationPos.getZ());

        nbt.putInt("keystoneX", keystonePos.getX());
        nbt.putInt("keystoneY", keystonePos.getY());
        nbt.putInt("keystoneZ", keystonePos.getZ());

        nbt.putString("dimension", dimension);

        super.writeNbt(nbt, lookupProvider);
    }

    // read NBT
    @Override
    public void readNbtImpl(NbtCompound nbt, Provider lookupProvider)
    {
        super.readNbtImpl(nbt, lookupProvider);

        destinationPos = new BlockPos(nbt.getInt("destX"), nbt.getInt("destY"), nbt.getInt("destZ"));
        keystonePos = new BlockPos(nbt.getInt("keystoneX"), nbt.getInt("keystoneY"), nbt.getInt("keystoneZ"));
        dimension = nbt.getString("dimension");
    }

    public static void tick(World world, BlockPos pos, BlockState state, CarvedRunestoneBlockEntity be)
    {
        if (world.isClient)
        {
            if (state.get(CarvedRunestoneBlock.GLOWING).booleanValue())
            {
                playActiveParticles((ClientWorld)world, pos);
            }

            if (be.ticks == 0)
            {
                be.searchAndAssignKeystone();
            }
            // playBeamSwirlParticles((ClientWorld)world, be.keystonePos);
        }

        be.animationTick++;
        be.ticks++;
    }

    @Environment(EnvType.CLIENT)
    private static void playActiveParticles(ClientWorld world, BlockPos pos)
    {
        if (world.getTime() % 4 != 0)
        {
            return;
        }

        // double angleIncrement = 2 * Math.PI / (2 + world.random.nextInt(5));
        // for (int i = 0; i < 12; i++) {
        double angle = /* (i * angleIncrement) + */(world.random.nextFloat() * 360);
        float x = (float)(0.5 * Math.cos(angle));
        float z = (float)(0.5 * Math.sin(angle));

        ModParticles.RUNESTONE_IDLE.spawn(world, pos.getX() + x + 0.5f, pos.getY(), pos.getZ() + z + 0.5f, 0, MathHelper.nextBetween(world.random, 0.1f, 0.2f), 0);
        // }
    }

    @Environment(EnvType.CLIENT)
    private static void playBeamSwirlParticles(ClientWorld world, BlockPos pos)
    {
        final int count = 20;
        final double dist = 2.0; 

        final double angleIncrement = 2 * Math.PI / ((double)count);
        final double offset = ((double)world.getTime() / 10);
        
        for (int i = 0; i < count; i++)
        {
            double angle = ((i * angleIncrement) + offset) % 360;
            float x = (float)(dist * Math.cos(angle));
            float z = (float)(dist * Math.sin(angle));

            ModParticles.BIFROST_BEAM.spawn(world, pos.getX() + x + 0.5f, pos.getY() + 1.3f, pos.getZ() + z + 0.5f, 0, MathHelper.nextBetween(world.random, 0.8f, 1.2f), 0);
        }
    }
}
