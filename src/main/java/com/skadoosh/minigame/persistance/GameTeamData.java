package com.skadoosh.minigame.persistance;

import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import com.skadoosh.wilderlands.persistance.NbtValue;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GameTeamData implements AutoSyncedComponent
{
    private final Team team;
    private final Scoreboard scoreboard;
    private final MinecraftServer server;

    private final NbtValue<Integer> flagCaptureCount = new NbtValue<Integer>("flag_capture_count",  0);
    private final NbtValue<Integer> markedKills = new NbtValue<Integer>("marked_kills",  0);

    private final NbtWorldPosValue baseLocation = new NbtWorldPosValue("base_location", new BlockPos(0, 0, 0), Identifier.ofDefault("overworld"));

    public NbtWorldPosValue getBaseLocation()
    {
        return baseLocation;
    }

    public int getTotalScore()
    {
        return flagCaptureCount.get() + markedKills.get();
    }

    public void addCapture()
    {
        flagCaptureCount.set(flagCaptureCount.get() + 1);
    }

    public void addKill()
    {
        markedKills.set(markedKills.get() + 1);
    }

    public GameTeamData(Team team, Scoreboard scoreboard, @Nullable MinecraftServer server)
    {
        this.team = team;
        this.scoreboard = scoreboard;
        this.server = server;
    }

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        flagCaptureCount.read(tag);
        markedKills.read(tag);
        baseLocation.read(tag);
    }
    
    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        flagCaptureCount.write(tag);
        markedKills.write(tag);
        baseLocation.write(tag);
    }

}
