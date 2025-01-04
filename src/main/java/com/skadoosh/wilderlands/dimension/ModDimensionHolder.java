package com.skadoosh.wilderlands.dimension;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensionHolder
{
    public final RegistryKey<DimensionOptions> options;
    public final RegistryKey<World> worldKey;
    public final RegistryKey<DimensionType> dimensionType;

    public ModDimensionHolder(Identifier id)
    {
        this.options = RegistryKey.of(RegistryKeys.DIMENSION, id);
        this.worldKey = RegistryKey.of(RegistryKeys.WORLD, id);
        this.dimensionType = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id);
    }
}
