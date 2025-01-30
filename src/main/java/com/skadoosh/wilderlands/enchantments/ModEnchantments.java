package com.skadoosh.wilderlands.enchantments;

import com.mojang.serialization.MapCodec;
import com.skadoosh.mcutils.datagen.annotations.AutoTranslate;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.enchantments.EnchantmentLevel.Level;
import com.skadoosh.wilderlands.enchantments.effects.Freeze;
import com.skadoosh.wilderlands.enchantments.effects.Lifesteal;
import com.skadoosh.wilderlands.enchantments.effects.StrengthenEffect;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModEnchantments
{
    @AutoTranslate("Frost Aspect")
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> FROST_ASPECT = keyOf(Wilderlands.id("frost_aspect"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Vampiric")
    public static final RegistryKey<Enchantment> VAMPIRIC = keyOf(Wilderlands.id("vampiric"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Lifelinked")
    public static final RegistryKey<Enchantment> LIFELINKED = keyOf(Wilderlands.id("lifelinked"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Butchering")
    public static final RegistryKey<Enchantment> BUTCHERING = keyOf(Wilderlands.id("butchering"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Lumberjack")
    public static final RegistryKey<Enchantment> LUMBERJACK = keyOf(Wilderlands.id("lumberjack"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Beheading")
    public static final RegistryKey<Enchantment> BEHEADING = keyOf(Wilderlands.id("beheading"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Molten")
    public static final RegistryKey<Enchantment> MOLTEN = keyOf(Wilderlands.id("molten"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Prospector")
    public static final RegistryKey<Enchantment> PROSPECTOR = keyOf(Wilderlands.id("prospector"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Voiding")
    public static final RegistryKey<Enchantment> VOIDING = keyOf(Wilderlands.id("voiding"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Amphibious")
    public static final RegistryKey<Enchantment> AMPHIBIOUS = keyOf(Wilderlands.id("amphibious"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Dextrous")
    public static final RegistryKey<Enchantment> DEXTROUS = keyOf(Wilderlands.id("dextrous"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Lift")
    public static final RegistryKey<Enchantment> LIFT = keyOf(Wilderlands.id("lift"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Springy")
    public static final RegistryKey<Enchantment> SPRINGY = keyOf(Wilderlands.id("springy"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Dash")
    public static final RegistryKey<Enchantment> DASH = keyOf(Wilderlands.id("dash"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Slide")
    public static final RegistryKey<Enchantment> SLIDE = keyOf(Wilderlands.id("slide"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Adrenaline")
    public static final RegistryKey<Enchantment> ADRENALINE = keyOf(Wilderlands.id("adrenaline"));
    public static final float ADRENALINE_MAX_SPEED_BOOST = 0.09f;
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Aerodynamic")
    public static final RegistryKey<Enchantment> AERODYNAMIC = keyOf(Wilderlands.id("aerodnamic"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Smoldering")
    public static final RegistryKey<Enchantment> SMOLDERING = keyOf(Wilderlands.id("smoldering"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Stonespined")
    public static final RegistryKey<Enchantment> STONESPINED = keyOf(Wilderlands.id("stonespined"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Masking")
    public static final RegistryKey<Enchantment> MASKING = keyOf(Wilderlands.id("masking"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Veil")
    public static final RegistryKey<Enchantment> VEIL = keyOf(Wilderlands.id("veil"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Illuminating")
    public static final RegistryKey<Enchantment> ILLUMINTING = keyOf(Wilderlands.id("illuminating"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Scooping")
    public static final RegistryKey<Enchantment> SCOOPING = keyOf(Wilderlands.id("scooping"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Puncturing")
    public static final RegistryKey<Enchantment> PUNCTURING = keyOf(Wilderlands.id("puncturing"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Grasping")
    public static final RegistryKey<Enchantment> GRASPING = keyOf(Wilderlands.id("grasping"));
    
    @EnchantmentLevel(Level.A)
    @AutoTranslate("Pinpoint")
    public static final RegistryKey<Enchantment> PINPOINT = keyOf(Wilderlands.id("pinpoint"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Quickdraw")
    public static final RegistryKey<Enchantment> QUICKDRAW = keyOf(Wilderlands.id("quickdraw"));
    
    @EnchantmentLevel(Level.B)
    @AutoTranslate("Reselient")
    public static final RegistryKey<Enchantment> RESILIENT = keyOf(Wilderlands.id("resilient"));
    
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
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> FEATHER_FALLING = Enchantments.FEATHER_FALLING;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> FIRE_ASPECT = Enchantments.FIRE_ASPECT;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> PUNCH = Enchantments.PUNCH;
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> INFINITY = Enchantments.INFINITY;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> LOYALTY = Enchantments.LOYALTY;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> RIPTIDE = Enchantments.RIPTIDE;
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> CHANNELING = Enchantments.CHANNELING;
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> MULTISHOT = Enchantments.MULTISHOT;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> QUICK_CHARGE = Enchantments.QUICK_CHARGE;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> PIERCING = Enchantments.PIERCING;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> DENSITY = Enchantments.DENSITY;
    
    @EnchantmentLevel(Level.A)
    public static final RegistryKey<Enchantment> BREACH = Enchantments.BREACH;
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> WIND_BURST = Enchantments.WIND_BURST;
    
    @EnchantmentLevel(Level.B)
    public static final RegistryKey<Enchantment> SILK_TOUCH = Enchantments.SILK_TOUCH;

    public static void init()
    {

    }

    public static final List<RegistryKey<Enchantment>> TO_REMOVE = List.of(
        Enchantments.PROTECTION,
        Enchantments.FIRE_PROTECTION,
        // Enchantments.FEATHER_FALLING,
        Enchantments.BLAST_PROTECTION,
        Enchantments.PROJECTILE_PROTECTION,
        Enchantments.RESPIRATION,
        Enchantments.AQUA_AFFINITY,
        Enchantments.THORNS,
        Enchantments.DEPTH_STRIDER,
        Enchantments.FROST_WALKER,
        Enchantments.BINDING_CURSE,
        Enchantments.SOUL_SPEED,
        Enchantments.SWIFT_SNEAK,
        Enchantments.SHARPNESS,
        Enchantments.SMITE,
        Enchantments.BANE_OF_ARTHROPODS,
        Enchantments.KNOCKBACK,
        // Enchantments.FIRE_ASPECT,
        Enchantments.LOOTING,
        Enchantments.SWEEPING_EDGE,
        Enchantments.EFFICIENCY,
        // Enchantments.SILK_TOUCH,
        Enchantments.UNBREAKING,
        Enchantments.FORTUNE,
        Enchantments.POWER,
        // Enchantments.PUNCH,
        Enchantments.FLAME,
        // Enchantments.INFINITY,
        Enchantments.LUCK_OF_THE_SEA,
        Enchantments.LURE,
        // Enchantments.LOYALTY,
        Enchantments.IMPALING,
        // Enchantments.RIPTIDE,
        // Enchantments.CHANNELING,
        // Enchantments.MULTISHOT,
        // Enchantments.QUICK_CHARGE,
        // Enchantments.PIERCING,
        // Enchantments.DENSITY,
        // Enchantments.BREACH,
        // Enchantments.WIND_BURST,
        Enchantments.MENDING,
        Enchantments.VANISHING_CURSE);
}
