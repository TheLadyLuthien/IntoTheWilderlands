package com.skadoosh.minigame.persistance;

import net.minecraft.nbt.NbtCompound;

public interface NbtReadWrite
{
    public void read(NbtCompound nbt);

    public void write(NbtCompound nbt);
}
