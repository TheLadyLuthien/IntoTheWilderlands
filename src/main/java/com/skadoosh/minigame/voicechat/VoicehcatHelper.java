package com.skadoosh.minigame.voicechat;

import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.skadoosh.minigame.Minigame;
import com.skadoosh.minigame.TeamRefrence;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.server.network.ServerPlayerEntity;

public class VoicehcatHelper
{
    public static boolean joinTeamGroup(ServerPlayerEntity player)
    {
        Group group = TeamRefrence.of(player).getData(player.getWorld()).getVoiceGroup();
        return setGroup(player, group);
    }

    public static boolean leaveTeamGroup(ServerPlayerEntity player)
    {
        return setGroup(player, null);
    }

    private static boolean setGroup(ServerPlayerEntity player, @Nullable Group group)
    {
        if (PluginEntrypoint.SERVER_API == null)
        {
            return false;
        }

        VoicechatConnection playerConnection = PluginEntrypoint.SERVER_API.getConnectionOf(player.getUuid());

        if (playerConnection == null || !playerConnection.isConnected())
        {
            return false;
        }

        playerConnection.setGroup(group);
        return true;
    }
}
