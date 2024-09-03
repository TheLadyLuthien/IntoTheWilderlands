package com.skadoosh.wilderlands.persistance;

import com.skadoosh.minigame.persistance.NbtReadWrite;

import net.minecraft.nbt.NbtCompound;

public class NbtValue<T> implements NbtReadWrite
{
    public final String name;
    private T value;

    public NbtValue(String name, T value)
    {
        this.name = name;
        this.value = value;
    }
    
    public void set(T val)
    {
        this.value = val;
    }

    public T get()
    {
        return this.value;
    }

    @Override
    public void write(NbtCompound nbt)
    {
        if (value instanceof Integer)
        {
            nbt.putInt(name, ((Integer)value).intValue());
        }
        else if (value instanceof Float)
        {
            nbt.putFloat(name, ((Float)value).floatValue());
        }
        else if (value instanceof Long)
        {
            nbt.putLong(name, ((Long)value).longValue());
        }
        else if (value instanceof String)
        {
            nbt.putString(name, ((String)value));
        }
        // TODO: expand to all basic types
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void read(NbtCompound nbt)
    {
        if (value instanceof Integer)
        {
            value = (T)Integer.valueOf(nbt.getInt(name)); 
        }
        else if (value instanceof Float)
        {
            value = (T)Float.valueOf(nbt.getFloat(name)); 
        }
        else if (value instanceof Long)
        {
            value = (T)Long.valueOf(nbt.getLong(name));
        }
        else if (value instanceof String)
        {
            value = (T)(nbt.getString(name)); 
        }
        // TODO: expand to all basic types
    }
}
