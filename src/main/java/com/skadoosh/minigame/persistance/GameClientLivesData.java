package com.skadoosh.minigame.persistance;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.ladysnake.cca.api.v3.component.ComponentKey;

import com.skadoosh.wilderlands.persistance.SyncableComponent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.scoreboard.Scoreboard;

public class GameClientLivesData extends SyncableComponent<GameClientLivesData, Scoreboard>
{
    private static final String LIVES = "lives";
    private final Map<UUID, Integer> data = new HashMap<>();
    
    public GameClientLivesData(ComponentKey<GameClientLivesData> componentKey, Scoreboard provider)
    {
        super(componentKey, provider);
    }

    public int getLives(UUID playerUUID)
    {
        return data.getOrDefault(playerUUID, GamePlayerData.STARTING_LIVES);
    }

    public void setLives(UUID playerUUID, int lives)
    {
        data.put(playerUUID, lives);
        sync();
    }

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        data.clear();
        NbtCompound compound = tag.getCompound(LIVES);
        if (compound != null)
        {
            for (String key : compound.getKeys())
            {
                int lives = compound.getInt(key);
                data.put(UUID.fromString(key), lives);
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        NbtCompound compound = new NbtCompound();
        for (UUID key : data.keySet())
        {
            int lives = data.get(key);
            compound.putInt(key.toString(), lives);
        }
        tag.put(LIVES, compound);
    }

}
