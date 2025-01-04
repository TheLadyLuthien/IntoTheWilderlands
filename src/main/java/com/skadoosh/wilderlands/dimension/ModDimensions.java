package com.skadoosh.wilderlands.dimension;

import java.util.OptionalLong;

import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.registry.BootstrapContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.int_provider.UniformIntProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionType.MonsterSettings;
import net.minecraft.world.dimension.DimensionTypes;

public class ModDimensions
{
    public static final ModDimensionHolder MOONLIT_GROVE = new ModDimensionHolder(Wilderlands.id("moonlit_grove"));

    public static void init()
    {
        
    }

    // public static void registerDimensions(BootstrapContext<Dimension> context)
    public static void registerTypes(BootstrapContext<DimensionType> context)
    {
        context.register(MOONLIT_GROVE.dimensionType, new DimensionType(
            OptionalLong.of(1350),
            true,
            false,
            false,
            true,
            0.2,
            true,
            true,
            -128,
            256,
            256,
            BlockTags.INFINIBURN_OVERWORLD,
            DimensionTypes.OVERWORLD_ID,
            0.3f,
            new MonsterSettings(false, false, UniformIntProvider.create(2, 12), 5)
        ));
    }
}