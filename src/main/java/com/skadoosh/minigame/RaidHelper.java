package com.skadoosh.minigame;

import com.skadoosh.minigame.persistance.GameTeamData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class RaidHelper
{
    public static boolean isOnRaid(PlayerEntity player)
    {
        TeamRefrence tr = TeamRefrence.of(player);
        return tr.isOnRaid(player.getWorld().getScoreboard());
    }
}
