package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.SmolderingComponent;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin
{
    @Inject(method = "onEntityDamaged", at = @At("HEAD"))
    private static void onEntityDamaged(ServerWorld world, Entity entity, DamageSource source, CallbackInfo ci)
    {
        if (source.getAttacker() instanceof PlayerEntity attacker && entity instanceof LivingEntity victom)
        {
            SmolderingComponent component = ModComponentKeys.SMOLDERING.get(attacker);
            if (component.hasEnchantment() && component.hasCharge())
            {
                double charge = component.expendCharge();
                victom.damage(world.getDamageSources().inFire(), (float)charge / 2.0f);
                victom.setFireTicks((int)(20.0 * charge));
            }
        }
    }
}
