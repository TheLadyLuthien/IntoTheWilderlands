package com.skadoosh.wilderlands.blockentities;

import java.util.EnumSet;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.RunicKeystoneBlock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
    public String getDimension()
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
}
