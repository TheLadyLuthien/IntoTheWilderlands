package com.skadoosh.minigame;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.RespawnableComponent;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class GamePlayerData implements AutoSyncedComponent, RespawnableComponent<GamePlayerData>
{
    public GamePlayerData(PlayerEntity player)
    {
        this.player = player;
    }

    private final PlayerEntity player;

    private static final String LIVES = "lives";
    private int lives = 5;
    
    public int getLives()
    {
        return lives;
    }

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        lives = tag.getInt(LIVES);
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        tag.putInt(LIVES, lives);
    }

    public void onMarkedDeath()
    {
        if (player instanceof ServerPlayerEntity)
        {
            final var sp = ((ServerPlayerEntity)player);
            lives--;
            if (lives <= 0)
            {
                // negative lives precaution
                lives = 0;
        
                // set to spectator
                sp.changeGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
