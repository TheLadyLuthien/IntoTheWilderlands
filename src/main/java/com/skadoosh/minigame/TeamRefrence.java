package com.skadoosh.minigame;

import com.skadoosh.minigame.ZoneHelper.ZoneType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.world.World;

public class TeamRefrence
{
    private static final NeutralTeam NEUTRAL_TEAM = new NeutralTeam();

    private final String teamId; // can be null to represent neutral team

    public static TeamRefrence of(String teamId)
    {
        if (teamId == null)
        {
            return NEUTRAL_TEAM;
        }
        return new TeamRefrence(teamId);
    }

    protected TeamRefrence(String teamId)
    {
        this.teamId = teamId;
    }

    public Team getTeam(World world)
    {
        if (teamId == null) return null;
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
