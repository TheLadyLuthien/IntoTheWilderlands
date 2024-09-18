package com.skadoosh.minigame.persistance;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.entity.RespawnableComponent;

import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.SyncableComponent;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class GamePlayerData extends SyncableComponent<GamePlayerData, PlayerEntity> implements RespawnableComponent<GamePlayerData>
{
    public GamePlayerData(PlayerEntity player, ComponentKey<GamePlayerData> key)
    {
        super(key, player);
        this.player = player;
    }

    private final PlayerEntity player;

    private static final String LIVES = "lives";
    public static final int STARTING_LIVES = 5;
    private int lives = STARTING_LIVES;

    // private static final String PREVIOUS_ZONE = "previous_zone";
    public static final String NO_ZONE = "no_zone";
    private String previousZone = NO_ZONE;

    private boolean voicechatInitialized = false;
    
    public boolean isVoicechatInitialized()
    {
        return voicechatInitialized;
    }

    public void setVoicechatInitialized(boolean voicechatInitialized)
    {
        this.voicechatInitialized = voicechatInitialized;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
        updateGlobalLifeInfo();
        sync();
    }

    public int getLives()
    {
        return lives;
    }

    public String getPreviousZoneId()
    {
        return previousZone.equals(NO_ZONE) ? null : previousZone;
    }

    public void setPreviousZoneId(String zoneId)
    {
        previousZone = (zoneId == null ? NO_ZONE : zoneId);
    }

    private void updateGlobalLifeInfo()
    {
        ModComponentKeys.GAME_CLIENT_LIVES_DATA.get(this.player.getScoreboard()).setLives(this.player.getUuid(), lives);
    }

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        lives = tag.getInt(LIVES);
        // previousZone = tag.getString(PREVIOUS_ZONE);
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        tag.putInt(LIVES, lives);
        // tag.putString(PREVIOUS_ZONE, previousZone);
    }

    public void onMarkedDeath()
    {
        if (player instanceof final ServerPlayerEntity sp)
        {
            lives--;
            if (lives <= 0)
            {
                // negative lives precaution
                lives = 0;
        
                // set to spectator
                sp.changeGameMode(GameMode.SPECTATOR);
            }

            player.sendMessage(Text.literal("Your death was marked. You have " + lives +  (lives == 1 ? " life" : " lives") + " remaining."), false);
            updateGlobalLifeInfo();
        }
        sync();
    }
}
