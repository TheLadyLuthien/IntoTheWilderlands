package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

import com.skadoosh.wilderlands.Wilderlands;

public final class ModComponentKeys implements WorldComponentInitializer, EntityComponentInitializer
{
    public static final ComponentKey<NamedKeystoneData> NAMED_KEYSTONE_DATA = ComponentRegistry.getOrCreate(Wilderlands.id("named_keystone_data"), NamedKeystoneData.class);
    public static final ComponentKey<LumberjackComponent> LUMBERJACK = ComponentRegistry.getOrCreate(Wilderlands.id("lumberjack"), LumberjackComponent.class);
    public static final ComponentKey<LiftComponent> LIFT = ComponentRegistry.getOrCreate(Wilderlands.id("lift"), LiftComponent.class);
    public static final ComponentKey<DashComponent> DASH = ComponentRegistry.getOrCreate(Wilderlands.id("dash"), DashComponent.class);
    public static final ComponentKey<SmolderingComponent> SMOLDERING = ComponentRegistry.getOrCreate(Wilderlands.id("smoldering"), SmolderingComponent.class);

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry)
    {
        registry.register(NAMED_KEYSTONE_DATA, world -> new NamedKeystoneData());
        registry.register(LUMBERJACK, LumberjackComponent::new);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
    {
        registry.registerForPlayers(LIFT, LiftComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(DASH, DashComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(SMOLDERING, SmolderingComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
    }


}
