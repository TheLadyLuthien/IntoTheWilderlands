package com.skadoosh.wilderlands.datagen;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.skadoosh.mcutils.datagen.AutoTranslate;
import com.skadoosh.mcutils.datagen.ModelGenerator;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.enchantments.EnchantmentLevel;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.items.itemGroup.ModItemGroups;
import com.skadoosh.wilderlands.misc.AnnotationHelper;
import com.skadoosh.wilderlands.misc.AnnotationHelper.ValueAnnotationPair;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.RegistryKey;
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
        pack.addProvider(EnchantmentTags::new);
        pack.addProvider(EnchantmentGenerator::new);
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(EnglishLanguageProvider::new);
    }

    public static class EnchantmentTags extends FabricTagProvider.EnchantmentTagProvider
    {
        public EnchantmentTags(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
        {
            super(output, completableFuture);
        }

        public static final TagKey<Enchantment> A_LEVEL = TagKey.of(RegistryKeys.ENCHANTMENT, Wilderlands.id("a_level_enchantments"));
        public static final TagKey<Enchantment> B_LEVEL = TagKey.of(RegistryKeys.ENCHANTMENT, Wilderlands.id("b_level_enchantments"));
        public static final TagKey<Enchantment> C_LEVEL = TagKey.of(RegistryKeys.ENCHANTMENT, Wilderlands.id("c_level_enchantments"));
        public static final TagKey<Enchantment> STAR_LEVEL = TagKey.of(RegistryKeys.ENCHANTMENT, Wilderlands.id("star_level_enchantments"));

        @SuppressWarnings("unchecked")
        @Override
        protected void configure(Provider wrapperLookup)
        {
            ArrayList<AnnotationHelper.ValueAnnotationPair<RegistryKey, EnchantmentLevel>> entries = AnnotationHelper.getFieldsWithAnnotation(EnchantmentLevel.class, ModEnchantments.class, RegistryKey.class);
            
            final var a = getOrCreateTagBuilder(A_LEVEL);
            final var b = getOrCreateTagBuilder(B_LEVEL);
            final var c = getOrCreateTagBuilder(C_LEVEL);
            final var star = getOrCreateTagBuilder(STAR_LEVEL);

            // a.add(ModEnchantments.FROST_ASPECT);

            for (var data : entries)
            {
                final var value = data.value;
                switch(data.annotation.value())
                {
                    case EnchantmentLevel.Level.A:
                    {
                        a.addOptional(value);
                        break;
                    }
                    case EnchantmentLevel.Level.B:
                    {
                        b.addOptional(value);
                        break;
                    }
                    case EnchantmentLevel.Level.C:
                    {
                        c.addOptional(value);
                        break;
                    }
                    case EnchantmentLevel.Level.STAR:
                    {
                        a.addOptional(value);
                        b.addOptional(value);
                        c.addOptional(value);
                        break;
                    }
                }

                star.addOptional(value);

                // Wilderlands.LOGGER.info("Datagen'd Block " + blockData.annotation.value());
                // tb.add(blockData.value, blockData.annotation.value());
            }
        }
    }

    public static class TagGenerator extends FabricTagProvider.ItemTagProvider
    {
        public TagGenerator(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
        {
            super(output, completableFuture);
        }

        public static final TagKey<Item> ALL_ITEMS = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("all_items"));

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
}
