package com.skadoosh.wilderlands.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.LevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record StrengthenEffect(LevelBasedValue duration, LevelBasedValue amplifier) implements EnchantmentEntityEffect
{
    public static final MapCodec<StrengthenEffect> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
            LevelBasedValue.CODEC.fieldOf("effectLevel").forGetter((strengthen) -> strengthen.amplifier())
        )
        .and(LevelBasedValue.CODEC.fieldOf("duration").forGetter((strengthen) -> strengthen.duration()))
        .apply(instance, StrengthenEffect::new);
    });

    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos)
    {
        if (!user.isAlive())
        {
            context.owner().addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, (int)(duration().getValue(level) * 20f), ((int)amplifier.getValue(level))));
        }
        // if (user instanceof LivingEntity)
    }

    public MapCodec<StrengthenEffect> getCodec()
    {
        return CODEC;
    }
}
