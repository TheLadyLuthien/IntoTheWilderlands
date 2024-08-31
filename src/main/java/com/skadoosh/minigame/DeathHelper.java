package com.skadoosh.minigame;

import com.skadoosh.minigame.ZoneHelper.ZoneType;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

public class DeathHelper {
    public static boolean shouldMarkDeath(PlayerEntity player, DamageSource damageSource)
    {
        ZoneType zone = ZoneHelper.getZoneType(player);

        if (zone == ZoneType.Enemy)
        {
            return true;
        }

        if (zone == ZoneType.Neutral && damageSource.getSource() instanceof PlayerEntity && !TeamRefrence.of((PlayerEntity)damageSource.getSource()).isFriendlyWith(player))
        {
            return true;
        }

        if (zone == ZoneType.Friendly && damageSource.getSource() instanceof PlayerEntity && RaidHelper.isOnRaid((PlayerEntity)damageSource.getSource()) && !TeamRefrence.of((PlayerEntity)damageSource.getSource()).isFriendlyWith(player))
        {
            return true;
        }

        return false;
    }
    public static boolean shouldMarkDeath(PlayerEntity player, PlayerEntity killer)
    {
        ZoneType zone = ZoneHelper.getZoneType(player);

        if (zone == ZoneType.Enemy)
        {
            return true;
        }

        if (zone == ZoneType.Neutral && !TeamRefrence.of(killer).isFriendlyWith(player))
        {
            return true;
        }

        if (zone == ZoneType.Friendly && RaidHelper.isOnRaid(killer) && !TeamRefrence.of(killer).isFriendlyWith(player))
        {
            return true;
        }

        return false;
    }

    public static boolean graveIsPublic(PlayerEntity player, DamageSource damageSource)
    {
        return shouldMarkDeath(player, damageSource);
    }

    public static Formatting getNameColor(int lives)
    {
        if (lives >= 4)
        {
            return Formatting.DARK_GREEN;
        }
        else if (lives >= 3)
        {
            return Formatting.GREEN;
        }
        else if (lives >= 2)
        {
            return Formatting.YELLOW;
        }
        else if (lives >= 1)
        {
            return Formatting.RED;
        }
        else 
        {
            return Formatting.GRAY;
        }
    }
}
