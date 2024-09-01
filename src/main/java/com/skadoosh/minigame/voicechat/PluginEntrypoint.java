package com.skadoosh.minigame.voicechat;

import org.jetbrains.annotations.Nullable;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.CreateGroupEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.PlayerConnectedEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;

public class PluginEntrypoint implements VoicechatPlugin
{
    @Nullable
    public static VoicechatServerApi SERVER_API;

    @Override
    public String getPluginId()
    {
        return "fall_games:voicechat_plugin";
    }

    @Override
    public void initialize(VoicechatApi api)
    {
    }

    @Override
    public void registerEvents(EventRegistration registration)
    {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }

    private void onServerStarted(VoicechatServerStartedEvent event)
    {
        SERVER_API = event.getVoicechat();
    }
}
