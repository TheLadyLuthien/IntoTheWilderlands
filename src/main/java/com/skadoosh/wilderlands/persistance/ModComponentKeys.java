package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

import com.skadoosh.minigame.GamePlayerData;
import com.skadoosh.minigame.GameWorldData;
import com.skadoosh.minigame.Minigame;
import com.skadoosh.wilderlands.Wilderlands;

public final class ModComponentKeys implements WorldComponentInitializer, EntityComponentInitializer
{
    public static final ComponentKey<NamedKeystoneData> NAMED_KEYSTONE_DATA = ComponentRegistry.getOrCreate(Wilderlands.id("named_keystone_data"), NamedKeystoneData.class);
    public static final ComponentKey<GamePlayerData> GAME_PLAYER_DATA = ComponentRegistry.getOrCreate(Minigame.id("game_player_data"), GamePlayerData.class);
    public static final ComponentKey<GameWorldData> GAME_WORLD_DATA = ComponentRegistry.getOrCreate(Minigame.id("game_world_data"), GameWorldData.class);

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry)
    {
        registry.register(NAMED_KEYSTONE_DATA, world -> new NamedKeystoneData());
        registry.register(GAME_WORLD_DATA, world -> new GameWorldData(world, GAME_WORLD_DATA));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
    {
        registry.registerForPlayers(GAME_PLAYER_DATA, GamePlayerData::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
