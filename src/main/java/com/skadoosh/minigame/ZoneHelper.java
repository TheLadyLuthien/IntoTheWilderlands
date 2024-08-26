package com.skadoosh.minigame;

import net.minecraft.entity.player.PlayerEntity;

public class ZoneHelper
{
    public static ZoneType getZoneType(PlayerEntity player)
    {
        if (player.getX() > 0)
        {
            return ZoneType.Enemy;
        }
        if (player.getZ() > 0)
        {
            return ZoneType.Neutral;
        }
        return ZoneType.Friendly;
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
