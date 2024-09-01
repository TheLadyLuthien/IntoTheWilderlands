package com.skadoosh.minigame.voicechat;

import java.util.Map;
import java.util.UUID;

import com.skadoosh.minigame.Minigame;
import com.skadoosh.minigame.TeamRefrence;

import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.ServerPlayer;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import net.minecraft.server.network.ServerPlayerEntity;

public class VoicehcatHelper
{
    public static void joinTeamGroup(ServerPlayerEntity player)
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

        try
        {
            var group = PluginEntrypoint.SERVER_API.getGroup(UUID.nameUUIDFromBytes(TeamRefrence.of(player).getName(player.getWorld()).getBytes()));

            if (group != null)
            {
                playerConnection.setGroup(group);
                Minigame.LOGGER.info(group.getId() + "");
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

        // playerConnection.setGroup(group.get);
    }

    public static void leaveTeamGroup(ServerPlayer player)
    {

    }
}
