package com.skadoosh.wilderlands.datagen;

import java.util.concurrent.CompletableFuture;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.enchantments.effects.Freeze;
import com.skadoosh.wilderlands.enchantments.effects.Lifesteal;
import com.skadoosh.wilderlands.enchantments.effects.StrengthenEffect;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.LevelBasedValue;
import net.minecraft.enchantment.effect.AddValue;
import net.minecraft.enchantment.effect.DamageEntity;
import net.minecraft.enchantment.effect.DamageImmunity;
import net.minecraft.enchantment.effect.EnchantmentAttribute;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.enchantment.effect.SetValue;
import net.minecraft.entity.EquipmentSlotGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.registry.HolderSet;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.HolderLookup.RegistryLookup;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.ItemTags;

public class EnchantmentGenerator extends FabricDynamicRegistryProvider
{
    public EnchantmentGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture)
    {
        super(output, registriesFuture);
    }

    @Override
    public String getName()
    {
        return "Wilderlands - Enchantment Generator";
    }

    private static void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... conditions)
    {
        entries.add(key, builder.build(key.getValue()), conditions);
    }

    private static Enchantment.Properties createDefaultProperties(HolderSet<Item> items, int maxLevel,  EquipmentSlotGroup... slots)
    {
        return Enchantment.createProperties(items, 10, maxLevel, Enchantment.cost(2), Enchantment.cost(2, 1), 2, slots);
    }

    @Override
    protected void configure(Provider registries, Entries entries)
    {
        RegistryLookup<Item> itemLookup = registries.getLookupOrThrow(RegistryKeys.ITEM);
        RegistryLookup<Enchantment> enchantLookup = registries.getLookupOrThrow(RegistryKeys.ENCHANTMENT);
        RegistryLookup<DamageType> damageTypeLookup = registries.getLookupOrThrow(RegistryKeys.DAMAGE_TYPE);
        
        final var A_LEVEL = enchantLookup.getTagOrThrow(Datagen.EnchantmentTags.A_LEVEL);
        final var B_LEVEL = enchantLookup.getTagOrThrow(Datagen.EnchantmentTags.B_LEVEL);
        final var C_LEVEL = enchantLookup.getTagOrThrow(Datagen.EnchantmentTags.C_LEVEL);
        final var STAR_LEVEL = enchantLookup.getTagOrThrow(Datagen.EnchantmentTags.STAR_LEVEL);

        register(entries, ModEnchantments.FROST_ASPECT, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK, // when to trigger
                EnchantmentEffectTarget.ATTACKER, // when who holds it
                EnchantmentEffectTarget.VICTIM, // who gets affected
                new Freeze(LevelBasedValue.linear(10, 5))
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.VAMPIRIC, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK, // when to trigger
                EnchantmentEffectTarget.ATTACKER, // when who holds it
                EnchantmentEffectTarget.ATTACKER, // who gets affected
                new Lifesteal(LevelBasedValue.constant(0.2f))
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.LIFELINKED, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.SWORD_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.DAMAGE, // when to trigger
                new AddValue(LevelBasedValue.linear(2, 2))
            )
            .addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK, // when to trigger
                EnchantmentEffectTarget.ATTACKER, // when who holds it
                EnchantmentEffectTarget.ATTACKER, // who gets affected
                new DamageEntity(LevelBasedValue.linear(2, 2), LevelBasedValue.linear(2, 2), damageTypeLookup.getHolderOrThrow(DamageTypes.WITHER))
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, Enchantments.SILK_TOUCH, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.BLOCK_EXPERIENCE,
                new SetValue(LevelBasedValue.constant(0.0F))
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.BUTCHERING, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.AXES),
                    1,
                    EquipmentSlotGroup.MAINHAND
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.ATTRIBUTES,
                new EnchantmentAttribute(
                    ModEnchantments.BUTCHERING.getValue(),
                    EntityAttributes.GENERIC_ATTACK_SPEED,
                    LevelBasedValue.linear(2, 1),
                    Operation.ADD_VALUE
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK, // when to trigger
                EnchantmentEffectTarget.ATTACKER, // when who holds it
                EnchantmentEffectTarget.ATTACKER, // who gets affected
                new StrengthenEffect(LevelBasedValue.linear(2, 0), LevelBasedValue.linear(0, 1))
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.LUMBERJACK, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.AXES),
                    1,
                    EquipmentSlotGroup.MAINHAND
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.BEHEADING, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.AXES),
                    1,
                    EquipmentSlotGroup.MAINHAND
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.PROSPECTOR, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.PICKAXES),
                    1,
                    EquipmentSlotGroup.MAINHAND
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.VOIDING, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.PICKAXES),
                    1,
                    EquipmentSlotGroup.MAINHAND
                )
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.MOLTEN, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.PICKAXES),
                    1,
                    EquipmentSlotGroup.MAINHAND
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, Enchantments.FEATHER_FALLING, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.FEET
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.DAMAGE_IMMUNITY,
                DamageImmunity.INSTANCE,
                DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().add(TagPredicate.expected(DamageTypeTags.IS_FALL)).add(TagPredicate.unexpected(DamageTypeTags.BYPASSES_INVULNERABILITY)))
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.AMPHIBIOUS, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.FEET
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.ATTRIBUTES,
                new EnchantmentAttribute(
                    ModEnchantments.AMPHIBIOUS.getValue(),
                    EntityAttributes.GENERIC_WATER_MOVEMENT_EFFICIENCY,
                    LevelBasedValue.linear(1.3f, 0.2f),
                    Operation.ADD_VALUE
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.ATTRIBUTES,
                new EnchantmentAttribute(
                    ModEnchantments.AMPHIBIOUS.getValue(),
                    EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED,
                    LevelBasedValue.linear(4, 4),
                    Operation.ADD_MULTIPLIED_TOTAL
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.DEXTROUS, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.FEET
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.ATTRIBUTES,
                new EnchantmentAttribute(
                    ModEnchantments.DEXTROUS.getValue(),
                    EntityAttributes.PLAYER_SNEAKING_SPEED,
                    LevelBasedValue.linear(0.4f, 0.2f),
                    Operation.ADD_VALUE
                )
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.LIFT, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                    3,
                    EquipmentSlotGroup.FEET
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.SPRINGY, 
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.FEET
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.ATTRIBUTES,
                new EnchantmentAttribute(
                    ModEnchantments.SPRINGY.getValue(),
                    EntityAttributes.GENERIC_JUMP_STRENGTH,
                    LevelBasedValue.linear(0.1f, 0.2f),
                    Operation.ADD_MULTIPLIED_BASE
                )
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.DASH,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                    3,
                    EquipmentSlotGroup.LEGS
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.SLIDE,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.LEGS
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.ADRENALINE,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.LEGS
                )
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.AERODYNAMIC,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.CHEST
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.SMOLDERING,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.CHEST
                )
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.STONESPINED,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.CHEST
                )
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, ModEnchantments.MASKING,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HEAD
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.VEIL,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HEAD
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.ILLUMINTING,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HEAD
                )
            )
            .withExclusiveSet(B_LEVEL)
        );
    }
}
