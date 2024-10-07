package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

import com.skadoosh.wilderlands.Wilderlands;

public final class ModComponentKeys implements WorldComponentInitializer
{
    public static final ComponentKey<NamedKeystoneData> NAMED_KEYSTONE_DATA = ComponentRegistry.getOrCreate(Wilderlands.id("named_keystone_data"), NamedKeystoneData.class);
    public static final ComponentKey<LumberjackComponent> LUMBERJACK = ComponentRegistry.getOrCreate(Wilderlands.id("lumberjack"), LumberjackComponent.class);

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry)
    {
        registry.register(NAMED_KEYSTONE_DATA, world -> new NamedKeystoneData());
        registry.register(LUMBERJACK, LumberjackComponent::new);
    }
}
