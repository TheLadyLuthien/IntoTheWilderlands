package com.skadoosh.wilderlands.datagen;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.skadoosh.mcutils.datagen.AutoTranslate;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.items.itemGroup.ModItemGroups;
import com.skadoosh.wilderlands.misc.AnnotationHelper;
import com.skadoosh.wilderlands.misc.AnnotationHelper.ValueAnnotationPair;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.HolderLookup.Provider;

class EnglishLanguageProvider extends FabricLanguageProvider
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

        handleAutoTranslateAnnotation(translationBuilder);

        // try
        // {
        // Path existingFilePath =
        // dataOutput.getModContainer().findPath("assets/mymod/lang/en_us.existing.json").get();
        // translationBuilder.add(existingFilePath);
        // }
        // catch (Exception e)
        // {
        // throw new RuntimeException("Failed to add existing language file!", e);
        // }
    }

    private static void handleAutoTranslateAnnotation(TranslationBuilder tb)
    {
        ArrayList<AnnotationHelper.ValueAnnotationPair<Block, AutoTranslate>> blocks = AnnotationHelper.getFieldsWithAnnotation(AutoTranslate.class, ModBlocks.class, Block.class);
        for (var blockData : blocks)
        {
            Wilderlands.LOGGER.info("Datagen'd Block " + blockData.annotation.value());
            tb.add(blockData.value, blockData.annotation.value());
        }

        ArrayList<AnnotationHelper.ValueAnnotationPair<Item, AutoTranslate>> items = AnnotationHelper.getFieldsWithAnnotation(AutoTranslate.class, ModItems.class, Item.class);
        for (var itemData : items)
        {
            tb.add(itemData.value, itemData.annotation.value());
        }
        
        ArrayList<AnnotationHelper.ValueAnnotationPair<String, AutoTranslate>> itemGroups = AnnotationHelper.getFieldsWithAnnotation(AutoTranslate.class, ModItemGroups.class, String.class);
        for (var itemGroupData : itemGroups)
        {
            tb.add(ModItemGroups.TRANSLATION_KEY_STUB + itemGroupData.value, itemGroupData.annotation.value());
        }

        // ArrayList<ValueAnnotationPair<RegistryKey, AutoTranslate>> enchantments = AnnotationHelper.getFieldsWithAnnotation(AutoTranslate.class, ModEnchantments.class, RegistryKey.class);
        // for (var enchantData : enchantments)
        // {
        //     tb.add(enchantData.value, enchantData.annotation.value());
        // }
    }
}