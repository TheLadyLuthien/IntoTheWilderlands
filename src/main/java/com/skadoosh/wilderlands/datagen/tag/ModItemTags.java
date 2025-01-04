package com.skadoosh.wilderlands.datagen.tag;

import java.util.concurrent.CompletableFuture;

import com.skadoosh.wilderlands.Wilderlands;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags extends FabricTagProvider.ItemTagProvider
{
    public ModItemTags(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
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