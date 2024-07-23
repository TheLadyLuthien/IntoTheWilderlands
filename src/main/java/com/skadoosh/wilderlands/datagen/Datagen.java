package com.skadoosh.wilderlands.datagen;

import java.util.concurrent.CompletableFuture;

import com.skadoosh.wilderlands.Wilderlands;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
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
}
