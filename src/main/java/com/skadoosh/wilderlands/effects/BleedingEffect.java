package com.skadoosh.wilderlands.effects;

import com.skadoosh.wilderlands.damage.ModDamageTypes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;

public class BleedingEffect extends StatusEffect
{
    public BleedingEffect()
    {
        super(StatusEffectType.HARMFUL, 0x800000, ParticleTypes.FALLING_LAVA);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier)
    {
        DamageSource damageSource = new DamageSource(entity.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.BLEEDING));
        entity.damage(damageSource, (amplifier + 1));

        return true;
    }

    @Override
    public boolean shouldApplyUpdateEffect(int tick, int amplifier)
    {
        return tick % 10 == 0;
    }
}
