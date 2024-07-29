package com.skadoosh.wilderlands.datagen;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import com.skadoosh.wilderlands.Wilderlands;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

public class Datagen implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        final FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(TagGenerator::new);
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(EnglishLanguageProvider::new);
    }

    private static class TagGenerator extends FabricTagProvider.ItemTagProvider
    {
        public TagGenerator(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
        {
            super(output, completableFuture);
        }

        private static final TagKey<Item> ALL_ITEMS = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("all_items"));

        @Override
        protected void configure(Provider wrapperLookup)
        {
            getOrCreateTagBuilder(ALL_ITEMS).add(Items.SLIME_BALL).add(Items.ROTTEN_FLESH).addOptionalTag(ItemTags.DIRT);
        }
    }

    private static class EnglishLanguageProvider extends FabricLanguageProvider
    {
        protected EnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookuper)
        {
            super(dataOutput, "en_us", registryLookuper);
        }

        @Override
        public void generateTranslations(Provider registryLookup, TranslationBuilder translationBuilder)
        {
            translationBuilder.add("tooltip.bifrost_key.title", "§3§lBifrost Key§r");
            translationBuilder.add("bifrost.colorized.dimension.overworld", "§bOverworld§r");
            translationBuilder.add("bifrost.colorized.dimension.the_nether", "§4Nether§r");
            translationBuilder.add("bifrost.colorized.dimension.the_end", "§dEnd§r");
            translationBuilder.add("bifrost.colorized.dimension.astral_wastes", "§7Astal Wastes§r");
            // try
            // {
            //     Path existingFilePath = dataOutput.getModContainer().findPath("assets/mymod/lang/en_us.existing.json").get();
            //     translationBuilder.add(existingFilePath);
            // }
            // catch (Exception e)
            // {
            //     throw new RuntimeException("Failed to add existing language file!", e);
            // }
        }
    }
}
