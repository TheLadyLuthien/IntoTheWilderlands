package com.skadoosh.wilderlands.datagen.tag;

import java.util.concurrent.CompletableFuture;

import com.skadoosh.wilderlands.Wilderlands;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class ModBlockTags extends FabricTagProvider.BlockTagProvider
{
    public ModBlockTags(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
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