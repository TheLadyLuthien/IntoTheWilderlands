package com.skadoosh.minigame.persistance;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import com.skadoosh.minigame.TeamRefrence;
import com.skadoosh.wilderlands.persistance.SyncableComponent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class GameWorldData extends SyncableComponent<GameWorldData, World>
{
    private final World world;

    private static final String TEAM_CHUNK_MAP = "team_chunk_map";
    private final HashMap<ChunkPos, String> teamChunkMap = new HashMap<>();

    public GameWorldData(World world, ComponentKey<GameWorldData> key)
    {
        super(key, world);
        this.world = world;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, Provider registryLookup)
    {
        NbtCompound compound = nbt.getCompound(TEAM_CHUNK_MAP);
        for (String longString : compound.getKeys())
        {
            String value = compound.getString(longString);
            long l = Long.parseLong(longString);

            this.teamChunkMap.put(new ChunkPos(l), value);
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt, Provider registryLookup)
    {
        NbtCompound compound = new NbtCompound();
        for (var pos : teamChunkMap.keySet())
        {
            String key = Long.toString(pos.toLong());
            String value = teamChunkMap.get(pos);

            compound.putString(key, value);
        }

        nbt.put(TEAM_CHUNK_MAP, compound);
    }

    public String getTeamIdAtChunk(ChunkPos pos)
    {
        return this.teamChunkMap.getOrDefault(pos, null);
    }

    public TeamRefrence getTeamAtChunk(ChunkPos pos)
    {
        return TeamRefrence.of(getTeamIdAtChunk(pos));
    }

    public void setTeamAtChunk(ChunkPos pos, String teamId)
    {
        if (teamId == null)
        {
            this.teamChunkMap.remove(pos);
        }
        else
        {
            this.teamChunkMap.put(pos, teamId);
        }

        sync();
    }
}
