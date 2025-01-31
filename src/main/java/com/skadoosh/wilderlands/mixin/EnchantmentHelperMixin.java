package com.skadoosh.wilderlands.mixin;

import java.util.List;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Lists;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.SmolderingComponent;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Holder;
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

    @Inject(method = "getPossibleLevelEntries", at = @At("HEAD"), cancellable = true)
    private static void getPossibleLevelEntries(int level, ItemStack stack, Stream<Holder<Enchantment>> possibleEnchantments, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir)
    {
        List<EnchantmentLevelEntry> list = Lists.<EnchantmentLevelEntry>newArrayList();
        boolean bl = stack.isOf(Items.BOOK);
        possibleEnchantments.filter(holder -> ((Enchantment)holder.value()).isPrimaryItem(stack) || bl).forEach(holder -> {
            Enchantment enchantment = (Enchantment)holder.value();
            list.add(new EnchantmentLevelEntry(holder, enchantment.getMaxLevel()));
        });

        cir.setReturnValue(list);
        cir.cancel();
        return;
    }
}
