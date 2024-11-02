package com.skadoosh.wilderlands.effects;

import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEffects
{
    public static final Holder<StatusEffect> BLEEDING = Registry.registerHolder(Registries.STATUS_EFFECT, Wilderlands.id("bleeding"), new BleedingEffect());
    
    public static void init()
    {
        
    }
}
