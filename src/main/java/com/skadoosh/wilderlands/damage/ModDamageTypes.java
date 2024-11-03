package com.skadoosh.wilderlands.damage;

import com.skadoosh.mcutils.datagen.NoKnockback;
import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModDamageTypes
{
    @NoKnockback
    public static final RegistryKey<DamageType> BLEEDING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Wilderlands.id("bleeding"));
    
    public static void init()
    {

    }
}
