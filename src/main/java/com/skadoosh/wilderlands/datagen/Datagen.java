package com.skadoosh.wilderlands.datagen;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.skadoosh.minigame.Minigame;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.items.itemGroup.ModItemGroups;
import com.skadoosh.wilderlands.misc.AnnotationHelper;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class Datagen implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        final FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(TagGenerator::new);
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(EnglishLanguageProvider::new);
        pack.addProvider(BlockLootTableGenerator::new);
    }

    private static class TagGenerator extends FabricTagProvider.BlockTagProvider
    {
        public TagGenerator(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
        {
            super(output, completableFuture);
        }

        private static final TagKey<Block> MINING_TOOL_HOE = TagKey.of(RegistryKeys.BLOCK, Identifier.ofDefault("mineable/hoe"));
        private static final TagKey<Block> MINING_TOOL_AXE = TagKey.of(RegistryKeys.BLOCK, Identifier.ofDefault("mineable/axe"));
        private static final TagKey<Block> MINING_TOOL_PICKAXE = TagKey.of(RegistryKeys.BLOCK, Identifier.ofDefault("mineable/pickaxe"));
        private static final TagKey<Block> MINING_TOOL_SHOVEL = TagKey.of(RegistryKeys.BLOCK, Identifier.ofDefault("mineable/shovel"));
        
        private static final TagKey<Block> REQUIRES_TOOL_STONE = TagKey.of(RegistryKeys.BLOCK, Identifier.ofDefault("mineable/needs_stone_tool"));
        private static final TagKey<Block> REQUIRES_TOOL_IRON = TagKey.of(RegistryKeys.BLOCK, Identifier.ofDefault("mineable/needs_iron_tool"));
        private static final TagKey<Block> REQUIRES_TOOL_DIAMOND = TagKey.of(RegistryKeys.BLOCK, Identifier.ofDefault("mineable/needs_diamond_tool"));

        @Override
        protected void configure(Provider wrapperLookup)
        {
            final FabricTagBuilder hoeTag = this.getOrCreateTagBuilder(MINING_TOOL_HOE);
            final FabricTagBuilder axeTag = this.getOrCreateTagBuilder(MINING_TOOL_AXE);
            final FabricTagBuilder pickaxeTag = this.getOrCreateTagBuilder(MINING_TOOL_PICKAXE);
            final FabricTagBuilder shovelTag = this.getOrCreateTagBuilder(MINING_TOOL_SHOVEL);
            
            final FabricTagBuilder stoneTag = this.getOrCreateTagBuilder(REQUIRES_TOOL_STONE);
            final FabricTagBuilder ironTag = this.getOrCreateTagBuilder(REQUIRES_TOOL_IRON);
            final FabricTagBuilder diamondTag = this.getOrCreateTagBuilder(REQUIRES_TOOL_DIAMOND);

            ArrayList<AnnotationHelper.ValueAnnotationPair<Block, AutoBlockLoot>> blocks = AnnotationHelper.getFieldsWithAnnotation(AutoBlockLoot.class, ModBlocks.class, Block.class);
            for (var blockData : blocks)
            {
                FabricTagBuilder prefferedTool = switch (blockData.annotation.prefersTool())
                {
                    case AXE -> axeTag;
                    case HOE -> hoeTag;
                    case PICKAXE -> pickaxeTag;
                    case SHOVEL -> shovelTag;
                    default -> null;
                };

                FabricTagBuilder requiredLevel = switch (blockData.annotation.requiresTool())
                {
                    case DIAMOND -> diamondTag;
                    case IRON -> ironTag;
                    case STONE -> stoneTag;
                    default -> null;
                };

                if (prefferedTool != null)
                {
                    prefferedTool.add(blockData.value);
                }
                if (requiredLevel != null)
                {
                    requiredLevel.add(blockData.value);
                }
            }
        }
    }

    private static class BlockLootTableGenerator extends FabricBlockLootTableProvider
    {
        protected BlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<Provider> registryLookup)
        {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate()
        {
            ArrayList<AnnotationHelper.ValueAnnotationPair<Block, AutoBlockLoot>> blocks = AnnotationHelper.getFieldsWithAnnotation(AutoBlockLoot.class, ModBlocks.class, Block.class);
            for (var blockData : blocks)
            {
                if (blockData.annotation.requireSilkTouch())
                {
                    this.addDropWithSilkTouch(blockData.value);
                }
                else
                {
                    this.addDrop(blockData.value);
                }
            }
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

            translationBuilder.add(Items.ELYTRA, "Rickety Glider");

            translationBuilder.add(Minigame.EVERSTAR, "The Everstar");
            translationBuilder.add(Minigame.GRAVE_TOKEN, "Grave Token");
            translationBuilder.add(Minigame.GRAVESTONE_BLOCK, "Gravestone");
            translationBuilder.add(Minigame.TEAM_BASE, "Team Base");


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
        }
    }
}
