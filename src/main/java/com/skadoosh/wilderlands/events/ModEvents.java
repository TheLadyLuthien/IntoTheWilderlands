package com.skadoosh.wilderlands.events;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.skadoosh.wilderlands.datagen.tag.ModBlockTags;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.enchantments.effects.lumberjack.LumberjackEvent;
import com.skadoosh.wilderlands.misc.BeheadingEntry;
import com.skadoosh.wilderlands.misc.BlockTagLootCondition;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.StopSleeping;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSubPredicateTypes;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.InvertedLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.NumberRange.IntRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.unmapped.C_loxplxmp;
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

        LootTableEvents.MODIFY.register((lootTableKey, builder, source, lookup) -> {
            final var blockList = lookup.getLookupOrThrow(RegistryKeys.BLOCK).getTagOrThrow(ModBlockTags.VOIDABLE);
            // if (Blocks.STONE.getLootTableId() == lootTableKey)
            final var keyList = blockList.stream().map(holder -> holder.value().getLootTableId()).toList();
            if (keyList.contains(lootTableKey));
            {
                builder.modifyPools(e -> {
                    e.conditionally(
                        new InvertedLootCondition(
                            AllOfLootCondition.create(List.of(
                                new MatchToolLootCondition(
                                    Optional.of(
                                        ItemPredicate.Builder.create()
                                            .method_58179(
                                                ItemSubPredicateTypes.ENCHANTMENTS,
                                                C_loxplxmp.C_zqrrydyv.method_58173(List.of(
                                                    new EnchantmentPredicate(
                                                        lookup.getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.VOIDING),
                                                        IntRange.atLeast(1)
                                                    )
                                                ))
                                            )
                                        .build()
                                    )
                                ),
                                new BlockTagLootCondition(ModBlockTags.VOIDABLE)
                            ))
                        )
                    );
                });
            }
        });
    }
}
