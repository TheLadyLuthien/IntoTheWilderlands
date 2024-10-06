package com.skadoosh.wilderlands.datagen;

import java.util.concurrent.CompletableFuture;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.enchantments.effects.Freeze;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.LevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.EquipmentSlotGroup;
import net.minecraft.item.Item;
import net.minecraft.registry.HolderSet;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.HolderLookup.RegistryLookup;
import net.minecraft.registry.tag.ItemTags;

public class EnchantmentGenerator extends FabricDynamicRegistryProvider
{
    public EnchantmentGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture)
    {
        super(output, registriesFuture);
    }

    @Override
    public String getName()
    {
        return "Wilderlands - Enchantment Generator";
    }

    private static void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... conditions)
    {
        entries.add(key, builder.build(key.getValue()), conditions);
    }

    private static Enchantment.Properties createDefaultProperties(HolderSet<Item> items, int maxLevel,  EquipmentSlotGroup... slots)
    {
        return Enchantment.createProperties(items, 10, maxLevel, Enchantment.cost(2), Enchantment.cost(2, 1), 2, slots);
    }

    @Override
    protected void configure(Provider registries, Entries entries)
    {
        RegistryLookup<Item> itemLookup = registries.getLookupOrThrow(RegistryKeys.ITEM);
        RegistryLookup<Enchantment> enchantLookup = registries.getLookupOrThrow(RegistryKeys.ENCHANTMENT);
        
        final var A_LEVEL = enchantLookup.getTagOrThrow(Datagen.EnchantmentTags.A_LEVEL);
        final var B_LEVEL = enchantLookup.getTagOrThrow(Datagen.EnchantmentTags.B_LEVEL);
        final var C_LEVEL = enchantLookup.getTagOrThrow(Datagen.EnchantmentTags.C_LEVEL);
        final var STAR_LEVEL = enchantLookup.getTagOrThrow(Datagen.EnchantmentTags.STAR_LEVEL);

        register(entries, ModEnchantments.FROST_ASPECT, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK, 
                EnchantmentEffectTarget.ATTACKER,
                EnchantmentEffectTarget.VICTIM,
                new Freeze(LevelBasedValue.linear(10, 5))
            )
            .withExclusiveSet(A_LEVEL)
        );
    }

}
