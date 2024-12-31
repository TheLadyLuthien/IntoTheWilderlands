package com.skadoosh.wilderlands.datagen;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.skadoosh.mcutils.datagen.AutoTranslate;
import com.skadoosh.mcutils.datagen.ModelGenerator;
import com.skadoosh.mcutils.datagen.DamageTypeTag.VanillaDamageTags;
import com.skadoosh.mcutils.datagen.DamageTypeTag;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.damage.ModDamageTypes;
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
import net.minecraft.block.Blocks;
import net.minecraft.data.server.tag.DamageTypeTagsProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class Datagen implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator)
    {
        final FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(BlockTagGenerator::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(EnchantmentTags::new);
        pack.addProvider(DamageTypeTagGenerator::new);
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

    public static class BlockTagGenerator extends FabricTagProvider.BlockTagProvider
    {
        public BlockTagGenerator(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
        {
            super(output, completableFuture);
        }

        public static final TagKey<Block> ORES = TagKey.of(RegistryKeys.BLOCK, Wilderlands.id("ores"));
        public static final TagKey<Block> VOIDABLE = TagKey.of(RegistryKeys.BLOCK, Wilderlands.id("voidable"));
        public static final TagKey<Block> AUTOSMELTS = TagKey.of(RegistryKeys.BLOCK, Wilderlands.id("autosmelts"));

        @Override
        protected void configure(Provider wrapperLookup)
        {
            getOrCreateTagBuilder(ORES)
            .addOptionalTag(BlockTags.COAL_ORES)
            .addOptionalTag(BlockTags.GOLD_ORES)
            .addOptionalTag(BlockTags.IRON_ORES)
            .addOptionalTag(BlockTags.LAPIS_ORES)
            .addOptionalTag(BlockTags.COPPER_ORES)
            .addOptionalTag(BlockTags.DIAMOND_ORES)
            .addOptionalTag(BlockTags.EMERALD_ORES)
            .addOptionalTag(BlockTags.REDSTONE_ORES)
            .add(Blocks.NETHER_QUARTZ_ORE)
            .add(Blocks.ANCIENT_DEBRIS);

            getOrCreateTagBuilder(VOIDABLE)
            .addOptionalTag(BlockTags.BASE_STONE_NETHER)
            .addOptionalTag(BlockTags.BASE_STONE_OVERWORLD)
            .addOptionalTag(BlockTags.DIRT)
            .add(Blocks.COBBLESTONE)
            .add(Blocks.BLACKSTONE)
            .add(Blocks.COBBLED_DEEPSLATE);
            
            getOrCreateTagBuilder(AUTOSMELTS)
            .addOptionalTag(ORES)
            .add(Blocks.STONE)
            .add(Blocks.COBBLESTONE)
            .add(Blocks.WET_SPONGE)
            .add(Blocks.SAND);
        }
    }
    public static class ItemTagGenerator extends FabricTagProvider.ItemTagProvider
    {
        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
        {
            super(output, completableFuture);
        }

        public static final TagKey<Item> ASTRAL_FORGE_REJECTED = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("astral_forge_rejected"));
        
        public static final TagKey<Item> ASTRAL_FORGE_TO_SINGLE_DESTINATION = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("astral_forge_to_single_destination"));
        public static final TagKey<Item> ASTRAL_FORGE_TO_FROM_SINGLE_DESTINATION = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("astral_forge_to_from_single_destination"));
        public static final TagKey<Item> ASTRAL_FORGE_TO_SINGLE_DIMENSION = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("astral_forge_to_single_dimension"));
        public static final TagKey<Item> ASTRAL_FORGE_WITHIN_CURRENT_DIMENSION = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("astral_forge_within_current_dimension"));
        public static final TagKey<Item> ASTRAL_FORGE_UNIVERSAL = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("astral_forge_universal"));


        @Override
        protected void configure(Provider wrapperLookup)
        {
            getOrCreateTagBuilder(ASTRAL_FORGE_REJECTED)
            .add(Items.ENDER_PEARL)
            .add(Items.ENDER_EYE)
            .add(Items.SPLASH_POTION)
            .add(Items.POTION)
            .add(Items.LINGERING_POTION)
            .add(Items.WIND_CHARGE)
            .add(Items.FIRE_CHARGE)
            .add(Items.EXPERIENCE_BOTTLE)
            .add(Items.EGG)
            .add(Items.END_CRYSTAL)
            .add(Items.FIREWORK_ROCKET)
            .add(Items.ITEM_FRAME)
            .add(Items.GLOW_ITEM_FRAME)
            .add(Items.LEAD)
            .add(Items.MINECART)
            .addOptionalTag(ItemTags.BOATS)
            .addOptionalTag(ItemTags.ARROWS);

            getOrCreateTagBuilder(ASTRAL_FORGE_TO_SINGLE_DESTINATION)
            .add(Items.DIRT)
            .add(Items.GRASS_BLOCK);

            getOrCreateTagBuilder(ASTRAL_FORGE_TO_FROM_SINGLE_DESTINATION)
            .add(Items.DIAMOND)
            .add(Items.END_CRYSTAL);

            getOrCreateTagBuilder(ASTRAL_FORGE_TO_SINGLE_DIMENSION)
            .add(Items.BLAZE_ROD)
            .add(Items.NAUTILUS_SHELL);

            getOrCreateTagBuilder(ASTRAL_FORGE_WITHIN_CURRENT_DIMENSION)
            .add(Items.ENCHANTED_GOLDEN_APPLE);

            getOrCreateTagBuilder(ASTRAL_FORGE_UNIVERSAL)
            .add(Items.DRAGON_EGG);

        }
    }

    public static class DamageTypeTagGenerator extends FabricTagProvider<DamageType>
    {
        public DamageTypeTagGenerator(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
        {
            super(output, RegistryKeys.DAMAGE_TYPE, completableFuture);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void configure(Provider wrapperLookup)
        {
            ArrayList<AnnotationHelper.ValueAnnotationPair<RegistryKey, DamageTypeTag>> pairs = AnnotationHelper.getFieldsWithAnnotation(DamageTypeTag.class, ModDamageTypes.class, RegistryKey.class);
            for (var pair : pairs)
            {
                for (VanillaDamageTags tag : pair.annotation.value())
                {
                    TagKey<DamageType> key = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.ofDefault(tag.id));
                    var builder = getOrCreateTagBuilder(key);
                    builder.addOptional(pair.value);
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
