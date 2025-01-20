package com.skadoosh.wilderlands.entities;

import com.skadoosh.wilderlands.Wilderlands;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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
    public static final EntityType<TorchEntity> TORCH = register("torch", EntityType.Builder.<TorchEntity>create(TorchEntity::new, SpawnGroup.MISC).setDimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));
    public static final EntityType<BundleEntity> BUNDLE = register("bundle", EntityType.Builder.<BundleEntity>create(BundleEntity::new, SpawnGroup.MISC).setDimensions(EntityType.ARROW.getWidth(), EntityType.ARROW.getHeight()));
    public static final EntityType<BifrostBeamEntity> BIFROST_BEAM = register("bifrost_beam", EntityType.Builder.<BifrostBeamEntity>create(BifrostBeamEntity::new, SpawnGroup.MISC).setDimensions(6, 6));
    // public static final EntityType<MimicEntity> MIMIC = register("mimic", EntityType.Builder.<MimicEntity>create(MimicEntity::new, SpawnGroup.MONSTER).setDimensions(14, 14));

    public static void init()
    {
        // FabricDefaultAttributeRegistry.register(MIMIC, MimicEntity.createAttributes());
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder)
    {
        Identifier id = Wilderlands.id(name);
        EntityType<?> type = builder.build(name);
        return (EntityType<T>)Registry.register(Registries.ENTITY_TYPE, id, type);
    }
}
