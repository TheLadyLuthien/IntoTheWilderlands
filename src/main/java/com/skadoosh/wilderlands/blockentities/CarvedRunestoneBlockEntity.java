package com.skadoosh.wilderlands.blockentities;

import java.util.EnumSet;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.CarvedRunestoneBlock;
import com.skadoosh.wilderlands.blocks.RunicKeystoneBlock;
import com.skadoosh.wilderlands.misc.ModParticles;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CarvedRunestoneBlockEntity extends BlockEntity
{
    public CarvedRunestoneBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CARVED_RUNESTONE_BLOCK_ENTITY, pos, state);
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
    protected void method_11014(NbtCompound nbt, Provider lookupProvider)
    {
        super.method_11014(nbt, lookupProvider);

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
        }
    }
    
    @ClientOnly
    private static void playActiveParticles(ClientWorld world, BlockPos pos)
    {
        if (world.getTime() % 4 != 0)
        {
            return;
        }

        // double angleIncrement = 2 * Math.PI / (2 + world.random.nextInt(5));
        // for (int i = 0; i < 12; i++) {
            double angle = /* (i * angleIncrement) +  */(world.random.nextFloat() * 360);
            float x = (float) (0.5 * Math.cos(angle));
            float z = (float) (0.5 * Math.sin(angle));
            
            ModParticles.RUNESTONE_IDLE.spawn(world, pos.getX() + x + 0.5f, pos.getY(), pos.getZ() + z + 0.5f, 0, MathHelper.nextBetween(world.random, 0.1f, 0.2f), 0);
        // }
    }
}
