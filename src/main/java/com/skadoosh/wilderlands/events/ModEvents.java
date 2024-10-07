package com.skadoosh.wilderlands.events;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.enchantments.effects.lumberjack.LumberjackEvent;
import com.skadoosh.wilderlands.misc.BeheadingEntry;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.StopSleeping;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;

public final class ModEvents
{
    public static void register()
    {
        EntitySleepEvents.STOP_SLEEPING.register(new StopSleeping()
        {
            @Override
            public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos)
            {
                entity.heal(entity.getMaxHealth());
            }
        });

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (entity instanceof LivingEntity attacker)
            {
                if (EnchantmentHelper.getLevel(world.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.BUTCHERING), attacker.getMainHandStack()) > 0)
                {
                    attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 2, 0));
                }

                if (EnchantmentHelper.getLevel(world.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.BEHEADING), attacker.getMainHandStack()) > 0)
                {
                    for (EntityType<?> entityType : BeheadingEntry.DROP_MAP.keySet())
                    {
                        if (killedEntity.getType() == entityType)
                        {
                            BeheadingEntry entry = BeheadingEntry.DROP_MAP.get(entityType);
                            if (world.getRandom().nextFloat() < entry.chance())
                            {
                                ItemStack stack = entry.drop().copy();
                                if (stack.getItem() == Items.PLAYER_HEAD && killedEntity instanceof PlayerEntity player)
                                {
                                    stack.set(DataComponentTypes.PROFILE, new ProfileComponent(player.getGameProfile()));
                                }
                                ItemScatterer.spawn(world, killedEntity.getX(), killedEntity.getY(), killedEntity.getZ(), stack);
                            }
                        }
                    }
                }
            }
        });

        PlayerBlockBreakEvents.BEFORE.register(new LumberjackEvent());
    }
}
