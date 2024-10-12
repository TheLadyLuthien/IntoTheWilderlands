package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup.Provider;

public class SmolderingComponent implements ServerTickingComponent, AutoSyncedComponent, ClientTickingComponent
{
    public SmolderingComponent(PlayerEntity player)
    {
        this.player = player;
    }

    private final PlayerEntity player;
    private int fireDamageTaken = 0;
    
    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
    }

    @Override
    public void serverTick()
    {
        
    }

    @Override
    public void clientTick()
    {
        
    }
}
