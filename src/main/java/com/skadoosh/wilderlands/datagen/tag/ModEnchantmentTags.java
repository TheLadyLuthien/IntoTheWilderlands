package com.skadoosh.wilderlands.datagen.tag;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.enchantments.EnchantmentLevel;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.misc.AnnotationHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.TagKey;

public class ModEnchantmentTags extends FabricTagProvider.EnchantmentTagProvider
{
    public ModEnchantmentTags(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
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

        final var inEnchantingTable = getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE);

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
            inEnchantingTable.addOptional(value);

            // Wilderlands.LOGGER.info("Datagen'd Block " + blockData.annotation.value());
            // tb.add(blockData.value, blockData.annotation.value());
        }

        
    }
}