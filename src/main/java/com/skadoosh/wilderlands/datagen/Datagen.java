package com.skadoosh.wilderlands.datagen;

import com.skadoosh.mcutils.datagen.ModelGenerator;
import com.skadoosh.wilderlands.datagen.tag.ModBlockTags;
import com.skadoosh.wilderlands.datagen.tag.ModDamageTypeTags;
import com.skadoosh.wilderlands.datagen.tag.ModEnchantmentTags;
import com.skadoosh.wilderlands.datagen.tag.ModItemTags;
import com.skadoosh.wilderlands.dimension.ModDimensions;
import com.skadoosh.wilderlands.dimension.biome.ModBiomes;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistrySetBuilder;

public class Datagen implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        final FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModBlockTags::new);
        pack.addProvider(ModItemTags::new);
        pack.addProvider(ModEnchantmentTags::new);
        pack.addProvider(ModDamageTypeTags::new);
        pack.addProvider(EnchantmentGenerator::new);
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(EnglishLanguageProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder)
    {
        registryBuilder.add(RegistryKeys.DIMENSION_TYPE, ModDimensions::registerTypes);
        registryBuilder.add(RegistryKeys.BIOME, ModBiomes::register);
    }
}
