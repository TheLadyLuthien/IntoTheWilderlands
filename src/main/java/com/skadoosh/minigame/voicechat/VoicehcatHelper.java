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
    public static void joinTeamGroup(ServerPlayerEntity player)
    {
        Group group = TeamRefrence.of(player).getData(player.getWorld()).getVoiceGroup();
        setGroup(player, group);
    }

    public static void leaveTeamGroup(ServerPlayerEntity player)
    {
        setGroup(player, null);
    }

    private static void setGroup(ServerPlayerEntity player, @Nullable Group group)
    {
        if (PluginEntrypoint.SERVER_API == null)
        {
            return;
        }

        VoicechatConnection playerConnection = PluginEntrypoint.SERVER_API.getConnectionOf(player.getUuid());

        if (playerConnection == null)
        {
            return;
        }

        playerConnection.setGroup(group);
    }
}
