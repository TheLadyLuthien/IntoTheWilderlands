package com.skadoosh.minigame;

import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;

import xaero.map.controls.ControlsHandler;
import xaero.map.controls.ControlsRegister;

public class ZoneHelper
{
    public static ZoneType getZoneType(PlayerEntity player)
    {
        TeamRefrence team = getZone(player);
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

    public static TeamRefrence getZone(PlayerEntity player)
    {
        ChunkPos chunkPos = player.getChunkPos();
        TeamRefrence team = ModComponentKeys.GAME_WORLD_DATA.get(player.getWorld()).getTeamAtChunk(chunkPos);
        return team;
    }

    public static enum ZoneType
    {
        Friendly(true, Formatting.GREEN),
        Neutral(true, Formatting.GRAY),
        Enemy(false, Formatting.RED);

        public final boolean canBuild;
        private final Formatting color;

        private ZoneType(boolean canBuild, Formatting color)
        {
            this.canBuild = canBuild;
            this.color = color;
        }

        public MutableText styleText(MutableText text)
        {
            return text.formatted(this.color);
        }
    }
}
