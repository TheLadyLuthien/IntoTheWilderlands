package com.skadoosh.wilderlands.damage;

import com.skadoosh.mcutils.datagen.annotations.DamageTypeTag;
import com.skadoosh.mcutils.datagen.annotations.DamageTypeTag.VanillaDamageTags;
import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModDamageTypes
{
    @DamageTypeTag(VanillaDamageTags.NO_KNOCKBACK)
    public static final RegistryKey<DamageType> BLEEDING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Wilderlands.id("bleeding"));
    
    @DamageTypeTag({VanillaDamageTags.BYPASSES_COOLDOWN, VanillaDamageTags.BYPASSES_ENCHANTMENTS, VanillaDamageTags.BYPASSES_RESISTANCE, VanillaDamageTags.BYPASSES_SHIELD, VanillaDamageTags.BYPASSES_ARMOR})
    public static final RegistryKey<DamageType> BRIMSTONE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Wilderlands.id("brimstone"));
    
    public static void init()
    {

    }
}
