package com.skadoosh.wilderlands.misc;

import java.util.Comparator;

import com.skadoosh.wilderlands.entities.BifrostBeamEntity;

import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;

public class ModChunkTickets
{
    public static final ChunkTicketType<ChunkPos> BIFROST = ChunkTicketType.create("bifrost", Comparator.comparingLong(ChunkPos::toLong), BifrostBeamEntity.DEFAULT_DURATION * 3);
    
    public static void init()
    {

    }
}
