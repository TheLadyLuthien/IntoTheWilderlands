package com.skadoosh.wilderlands.components;

import java.util.function.UnaryOperator;

import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.component.DataComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModComponents
{
    // builder.codec(NbtComponent.CODEC).packetCodec(NbtComponent.PACKET_CODEC)
    // public static final DataComponentType<NbtComponent> BIFROST_KEY =
    // DataComponentType.builder().codec(NbtComponent.CODEC).packetCodec(NbtComponent.PACKET_CODEC).build();
    
    public static final DataComponentType<NbtComponent> BIFROST_KEY = register(Wilderlands.id("bifrost_key"), builder -> builder.codec(NbtComponent.CODEC).packetCodec(NbtComponent.PACKET_CODEC));

    public static void init()
    {
    }

    private static <T> DataComponentType<T> register(Identifier id, UnaryOperator<DataComponentType.Builder<T>> operator)
    {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, ((DataComponentType.Builder)operator.apply(DataComponentType.builder())).build());
    }
}
