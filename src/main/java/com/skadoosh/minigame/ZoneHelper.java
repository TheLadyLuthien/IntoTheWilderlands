package com.skadoosh.minigame;

import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;

public class ZoneHelper
{
    public static ZoneType getZoneType(PlayerEntity player)
    {
        ChunkPos chunkPos = player.getChunkPos();
        TeamRefrence team = ModComponentKeys.GAME_WORLD_DATA.get(player.getWorld()).getTeamAtChunk(chunkPos);
        if (team.hasMember(player) && team.isFriendlyWith(player))
        {
            return ZoneType.Friendly;
        }
        else if (team.isFriendlyWith(player))
        {
            return ZoneType.Neutral;
        }
        else
        {
            return ZoneType.Enemy;
        }
    }

    public static enum ZoneType
    {
        Friendly(true),
        Neutral(true),
        Enemy(false);

        public final boolean canBuild;

        private ZoneType(boolean canBuild)
        {
            this.canBuild = canBuild;
        }
    }
}
