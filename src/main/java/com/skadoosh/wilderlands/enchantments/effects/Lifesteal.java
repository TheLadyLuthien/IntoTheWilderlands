package com.skadoosh.wilderlands.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.LevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record Lifesteal(LevelBasedValue percent) implements EnchantmentEntityEffect
{
    public static final MapCodec<Lifesteal> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(LevelBasedValue.CODEC.fieldOf("percent").forGetter((lifesteal) -> {
            return lifesteal.percent();
        })).apply(instance, Lifesteal::new);
    });

    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos)
    {
        if (user instanceof LivingEntity entity)
        {
            double damage = entity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            damage *= percent.getValue(level);
            entity.heal((float)damage);
        }
    }

    public MapCodec<Lifesteal> getCodec()
    {
        return CODEC;
    }
}
