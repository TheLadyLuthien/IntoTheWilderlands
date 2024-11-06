package com.skadoosh.wilderlands.entities;

import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities
{
    public static EntityType<TorchEntity> TORCH = register("torch", EntityType.Builder.<TorchEntity>create(TorchEntity::new, SpawnGroup.MISC).setDimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));

    public static void init()
    {

    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder)
    {
        Identifier id = Wilderlands.id(name);
        EntityType<?> type = builder.build(name);
        return (EntityType<T>)Registry.register(Registries.ENTITY_TYPE, id, type);
    }
}
