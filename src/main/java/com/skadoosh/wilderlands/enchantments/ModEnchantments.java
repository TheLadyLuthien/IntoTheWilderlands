package com.skadoosh.wilderlands.enchantments;

import com.mojang.serialization.MapCodec;
import com.skadoosh.mcutils.datagen.AutoTranslate;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.enchantments.EnchantmentLevel.Level;
import com.skadoosh.wilderlands.enchantments.effects.Freeze;
import com.skadoosh.wilderlands.enchantments.effects.Lifesteal;
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
    public static final RegistryKey<Enchantment> MOLTEN = keyOf(Wilderlands.id("molten"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> PROSPECTOR = keyOf(Wilderlands.id("prospector"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> VOIDING = keyOf(Wilderlands.id("voiding"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> AMPHIBIOUS = keyOf(Wilderlands.id("amphibious"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> DEXTROUS = keyOf(Wilderlands.id("dextrous"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> LIFT = keyOf(Wilderlands.id("lift"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> SPRINGY = keyOf(Wilderlands.id("springy"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> DASH = keyOf(Wilderlands.id("dash"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> SLIDE = keyOf(Wilderlands.id("slide"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> ADRENALINE = keyOf(Wilderlands.id("adrenaline"));
    public static final float ADRENALINE_MAX_SPEED_BOOST = 0.09f;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> AERODYNAMIC = keyOf(Wilderlands.id("aerodnamic"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> SMOLDERING = keyOf(Wilderlands.id("smoldering"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> STONESPINED = keyOf(Wilderlands.id("stonespined"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> MASKING = keyOf(Wilderlands.id("masking"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> VEIL = keyOf(Wilderlands.id("veil"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> ILLUMINTING = keyOf(Wilderlands.id("illuminating"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> SCOOPING = keyOf(Wilderlands.id("scooping"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> PUNCTURING = keyOf(Wilderlands.id("puncturing"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> GRASPING = keyOf(Wilderlands.id("grasping"));
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> PINPOINT = keyOf(Wilderlands.id("pinpoint"));
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> QUICKDRAW = keyOf(Wilderlands.id("quickdraw"));
    
    public static final MapCodec<Freeze> FREEZE_EFFECT = registerEntityEffect(Wilderlands.id("freeze"), Freeze.CODEC);
    public static final MapCodec<StrengthenEffect> STRENTHEN_EFFECT = registerEntityEffect(Wilderlands.id("strengthen"), StrengthenEffect.CODEC);
    public static final MapCodec<Lifesteal> LIFESTEAL = registerEntityEffect(Wilderlands.id("lifesteal"), Lifesteal.CODEC);

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
