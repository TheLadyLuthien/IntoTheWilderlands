package com.skadoosh.wilderlands.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.LevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record Freeze(LevelBasedValue duration) implements EnchantmentEntityEffect
{
   public static final MapCodec<Freeze> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
      return instance.group(LevelBasedValue.CODEC.fieldOf("duration").forGetter((freeze) -> {
         return freeze.duration();
      })).apply(instance, Freeze::new);
   });

   public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos)
   {
      user.setFrozenTicks((int)(this.duration.getValue(level) * 20));
   }

   public MapCodec<Freeze> getCodec()
   {
      return CODEC;
   }

   public LevelBasedValue duration()
   {
      return this.duration;
   }
}
