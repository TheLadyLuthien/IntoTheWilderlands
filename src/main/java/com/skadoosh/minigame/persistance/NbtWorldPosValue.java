package com.skadoosh.minigame.persistance;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NbtWorldPosValue implements NbtReadWrite
{
    public final String name;

    private BlockPos pos;
    private String world;

    public NbtWorldPosValue(String name, BlockPos pos, Identifier world)
    {
        this.name = name;
        this.pos = pos;
        this.world = world.toString();
    }

    @Override
    public void read(NbtCompound nbt)
    {
        NbtCompound comp = nbt.getCompound(name);
        pos = new BlockPos(comp.getInt("x"), comp.getInt("y"), comp.getInt("z"));
        world = comp.getString("world");
    }
    
    @Override
    public void write(NbtCompound nbt)
    {
        NbtCompound comp = new NbtCompound();
        comp.putInt("x", pos.getX());
        comp.putInt("y", pos.getY());
        comp.putInt("z", pos.getZ());
        comp.putString("world", world);
        nbt.put(name, comp);
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public void setPos(BlockPos pos, World world)
    {
        this.pos = pos;
        this.world = world.getRegistryKey().getValue().toString();
    }

    public ServerWorld getWorld(MinecraftServer server)
    {
        Identifier identifier = Identifier.parse(this.world);
        RegistryKey<World> registryKey = RegistryKey.of(RegistryKeys.WORLD, identifier);
        return server.getWorld(registryKey);
    }
}
