package com.skadoosh.wilderlands.enchantments;

import com.mojang.serialization.MapCodec;
import com.skadoosh.mcutils.datagen.AutoTranslate;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.enchantments.EnchantmentLevel.Level;
import com.skadoosh.wilderlands.enchantments.effects.Freeze;
import com.skadoosh.wilderlands.enchantments.effects.StrengthenEffect;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEnchantments
{
    @AutoTranslate("Frost Aspect")
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> FROST_ASPECT = keyOf(Wilderlands.id("frost_aspect"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> VAMPIRIC = keyOf(Wilderlands.id("vampiric"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> LIFELINKED = keyOf(Wilderlands.id("lifelinked"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> BUTCHERING = keyOf(Wilderlands.id("butchering"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> LUMBERJACK = keyOf(Wilderlands.id("lumberjack"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> BEHEADING = keyOf(Wilderlands.id("beheading"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> PROSPECTOR = keyOf(Wilderlands.id("prospector"));
    
    public static final MapCodec<Freeze> FREEZE_EFFECT = registerEntityEffect(Wilderlands.id("freeze"), Freeze.CODEC);
    public static final MapCodec<StrengthenEffect> STRENTHEN_EFFECT = registerEntityEffect(Wilderlands.id("strengthen"), StrengthenEffect.CODEC);

    public static <T extends EnchantmentEntityEffect> MapCodec<T> registerEntityEffect(Identifier id, MapCodec<T> codec)
    {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, id, codec);
    }

    private static RegistryKey<Enchantment> keyOf(Identifier id)
    {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }

    public static void init()
    {

    }
}
