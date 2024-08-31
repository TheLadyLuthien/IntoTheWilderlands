package com.skadoosh.minigame;

import org.jetbrains.annotations.Nullable;

import com.skadoosh.minigame.persistance.GameTeamData;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.World;

public class TeamRefrence
{
    private static final NeutralTeam NEUTRAL_TEAM = new NeutralTeam();

    @Nullable
    private final String teamId; // can be null to represent neutral team

    public static TeamRefrence of(String teamId)
    {
        if (teamId == null)
        {
            return NEUTRAL_TEAM;
        }
        return new TeamRefrence(teamId);
    }

    public static TeamRefrence of(PlayerEntity player)
    {
        try
        {
            return of(player.getWorld().getScoreboard().getPlayerTeam(player.getProfileName()).getName());
        }
        catch (Exception e)
        {
            return NEUTRAL_TEAM;
        }
    }

    protected TeamRefrence(String teamId)
    {
        this.teamId = teamId;
    }

    public Team getTeam(World world)
    {
        if (teamId == null)
            return null;
        return world.getScoreboard().getTeam(teamId);
    }

    public boolean hasMember(PlayerEntity player)
    {
        return getTeam(player.getWorld()).getPlayerList().contains(player.getProfileName());
    }

    public boolean isFriendlyWith(PlayerEntity player)
    {
        return hasMember(player);
    }

    public GameTeamData getData(World world)
    {
        return ModComponentKeys.GAME_TEAM_DATA.get(getTeam(world));
    }

    public String getId()
    {
        return teamId;
    }

    public String getName(World world)
    {
        return getTeam(world).getDisplayName().getString();
    }

    public static class NeutralTeam extends TeamRefrence
    {
        protected NeutralTeam()
        {
            super(null);
        }

        @Override
        public Team getTeam(World world)
        {
            return null;
        }

        @Override
        public boolean hasMember(PlayerEntity player)
        {
            return false;
        }

        @Override
        public boolean isFriendlyWith(PlayerEntity player)
        {
            return true;
        }

        @Override
        public String getName(World world)
        {
            return "Neutral";
        }

        @Override
        public GameTeamData getData(World world)
        {
            return null;
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TeamRefrence other = (TeamRefrence)obj;
        if (teamId == null)
        {
            if (other.teamId != null)
                return false;
        }
        else if (!teamId.equals(other.teamId))
            return false;
        return true;
    }
}
