package com.skadoosh.wilderlands.datagen;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.effects.ModEffects;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.enchantments.effects.Freeze;
import com.skadoosh.wilderlands.enchantments.effects.Lifesteal;
import com.skadoosh.wilderlands.enchantments.effects.StrengthenEffect;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.block.Blocks;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.LevelBasedValue;
import net.minecraft.enchantment.effect.AddValue;
import net.minecraft.enchantment.effect.AllOfEffects;
import net.minecraft.enchantment.effect.ApplyMobEffect;
import net.minecraft.enchantment.effect.DamageEntity;
import net.minecraft.enchantment.effect.DamageImmunity;
import net.minecraft.enchantment.effect.EnchantmentAttribute;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.enchantment.effect.Explode;
import net.minecraft.enchantment.effect.Ignite;
import net.minecraft.enchantment.effect.PlaySound;
import net.minecraft.enchantment.effect.SetValue;
import net.minecraft.enchantment.effect.SummonEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlotGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.WeatherCheckLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContext.EntityTarget;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.HolderSet;
import net.minecraft.registry.HolderSet.NamedSet;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Holder;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.HolderLookup.RegistryLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.unmapped.C_rubkprmd;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.float_provider.ConstantFloatProvider;
import net.minecraft.world.World;

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

    private static Enchantment.Properties createBlockingProperties(HolderSet<Item> items)
    {
        return Enchantment.createProperties(items, 1, 1, Enchantment.cost(100), Enchantment.cost(100, 100), 100, new EquipmentSlotGroup[0]);
    }

    private static void blockEnchantment(Entries entries, RegistryKey<Enchantment> key, NamedSet<Enchantment> starKey, HolderSet<Item> items)
    {
        entries.add(key, Enchantment.builder(createBlockingProperties(items)).withExclusiveSet(starKey).build(key.getValue()), new ResourceCondition[0]);
    }

    private static final TagKey<Item> BLOCKING_KEY = TagKey.of(RegistryKeys.ITEM, Wilderlands.id("none"));

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

        final var blockingKey = itemLookup.getTagOrThrow(BLOCKING_KEY);
        for (var key : ModEnchantments.TO_REMOVE)
        {
            blockEnchantment(entries, key, STAR_LEVEL, blockingKey);   
        }

        register(entries, Enchantments.FIRE_ASPECT, 
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
                new Ignite(LevelBasedValue.linear(10, 5))
            )
            .withExclusiveSet(A_LEVEL)
        );

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

        register(entries, ModEnchantments.SCOOPING,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.SHOVELS),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addEffect(
                EnchantmentEffectComponentTypes.ATTRIBUTES,
                new EnchantmentAttribute(
                    Wilderlands.id("enchantment.scooping"),
                    EntityAttributes.PLAYER_MINING_EFFICIENCY,
                    new LevelBasedValue.Constant(26f),
                    Operation.ADD_VALUE
                )
            )
        );

        register(entries, Enchantments.RIPTIDE,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addSpecialEffect(
                EnchantmentEffectComponentTypes.TRIDENT_SPIN_ATTACK_STRENGTH,
                new AddValue(LevelBasedValue.constant(6))
            )
            .addSpecialEffect(
                EnchantmentEffectComponentTypes.TRIDENT_SOUND,
                List.of(SoundEvents.ITEM_TRIDENT_RIPTIDE_1, SoundEvents.ITEM_TRIDENT_RIPTIDE_2, SoundEvents.ITEM_TRIDENT_RIPTIDE_3)
            )
        );

        register(entries, Enchantments.LOYALTY,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addEffect(
                EnchantmentEffectComponentTypes.TRIDENT_RETURN_ACCELERATION,
                new AddValue(LevelBasedValue.perLevel(5.0F))
            )
        );

        register(entries, Enchantments.CHANNELING,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(B_LEVEL)
            .addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK,
                EnchantmentEffectTarget.ATTACKER,
                EnchantmentEffectTarget.VICTIM,
                AllOfEffects.entityEffects(
                    new EnchantmentEntityEffect[]{
                        new SummonEntity(
                            HolderSet.createDirect(
                                new Holder[]{EntityType.LIGHTNING_BOLT.getBuiltInRegistryHolder()}
                            ),
                            false
                        ),
                        new PlaySound(
                            SoundEvents.ITEM_TRIDENT_THUNDER,
                            ConstantFloatProvider.create(5.0F),
                            ConstantFloatProvider.create(1.0F)
                        )
                    }
                ),
                AllOfLootCondition.builder(
                    new LootCondition.Builder[]{
                        LocationCheckLootCondition.builder(net.minecraft.predicate.entity.LocationPredicate.Builder.create().canSeeSky(true)),
                    }
                )
            )
            .addEffect(
                EnchantmentEffectComponentTypes.HIT_BLOCK,
                AllOfEffects.entityEffects(
                    new EnchantmentEntityEffect[]{
                        new SummonEntity(
                            HolderSet.createDirect(new Holder[]{EntityType.LIGHTNING_BOLT.getBuiltInRegistryHolder()}),
                            false
                        ),
                        new PlaySound(
                            SoundEvents.ITEM_TRIDENT_THUNDER,
                            ConstantFloatProvider.create(5.0F),
                            ConstantFloatProvider.create(1.0F)
                        )
                    }
                ),
                AllOfLootCondition.builder(
                    new LootCondition.Builder[]{
                        EntityPropertiesLootCondition.builder(EntityTarget.THIS, net.minecraft.predicate.entity.EntityPredicate.Builder.create().type(EntityType.TRIDENT)),
                        LocationCheckLootCondition.builder(net.minecraft.predicate.entity.LocationPredicate.Builder.create().canSeeSky(true)),
                    }
                )
            )
        );

        register(entries, ModEnchantments.PUNCTURING,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(B_LEVEL)
            .addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK,
                EnchantmentEffectTarget.ATTACKER,
                EnchantmentEffectTarget.VICTIM,
                new ApplyMobEffect(HolderSet.createDirect(new Holder[]{ModEffects.BLEEDING}), LevelBasedValue.constant(5), LevelBasedValue.constant(5), LevelBasedValue.constant(0), LevelBasedValue.constant(0))
            )
        );

        register(entries, Enchantments.PUNCH,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.BOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addEffect(
                EnchantmentEffectComponentTypes.KNOCKBACK,
                new AddValue(LevelBasedValue.perLevel(2.0F)),
                EntityPropertiesLootCondition.builder(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.create().tagged(EntityTypeTags.ARROWS).build())
            )
        );

        register(entries, ModEnchantments.GRASPING,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.BOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addEffect(
                EnchantmentEffectComponentTypes.KNOCKBACK,
                new AddValue(LevelBasedValue.perLevel(-3.0F)),
                EntityPropertiesLootCondition.builder(LootContext.EntityTarget.DIRECT_ATTACKER, EntityPredicate.Builder.create().tagged(EntityTypeTags.ARROWS).build())
            )
        );

        register(entries, Enchantments.INFINITY,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.BOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(B_LEVEL)
            .addEffect(
                EnchantmentEffectComponentTypes.AMMO_USE,
                new SetValue(LevelBasedValue.constant(0.0F)),
                MatchToolLootCondition.builder(ItemPredicate.Builder.create().items(Items.ARROW))
            )
        );

        register(entries, ModEnchantments.PINPOINT,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.BOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
        );

        register(entries, ModEnchantments.QUICKDRAW,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.BOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, Enchantments.QUICK_CHARGE,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addSpecialEffect(EnchantmentEffectComponentTypes.CROSSBOW_CHARGE_TIME, new AddValue(LevelBasedValue.constant(-0.75F)))
            .addSpecialEffect(
                EnchantmentEffectComponentTypes.CROSSBOW_CHARGING_SOUNDS,
                List.of(
                    new CrossbowItem.LoadingSounds(
                        Optional.of(SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1), Optional.empty(), Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
                    ),
                    new CrossbowItem.LoadingSounds(
                        Optional.of(SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2), Optional.empty(), Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
                    ),
                    new CrossbowItem.LoadingSounds(
                        Optional.of(SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3), Optional.empty(), Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
                    )
                )
            )
        );

        register(entries, Enchantments.PIERCING,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addEffect(EnchantmentEffectComponentTypes.PROJECTILE_PIERCING, new AddValue(LevelBasedValue.constant(50.0F)))
        );

        register(entries, Enchantments.MULTISHOT,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(B_LEVEL)
            .addEffect(EnchantmentEffectComponentTypes.PROJECTILE_COUNT, new AddValue(LevelBasedValue.perLevel(2.0F)))
            .addEffect(EnchantmentEffectComponentTypes.PROJECTILE_SPREAD, new AddValue(LevelBasedValue.perLevel(10.0F)))
        );

        register(entries, ModEnchantments.RESILIENT,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(B_LEVEL)
        );

        register(entries, Enchantments.DENSITY,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.MACE_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addEffect(EnchantmentEffectComponentTypes.SMASH_DAMAGE_PER_FALLEN_BLOCK, new AddValue(LevelBasedValue.constant(2f)))
        );

        register(entries, Enchantments.BREACH,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.MACE_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addEffect(EnchantmentEffectComponentTypes.ARMOR_EFFECTIVENESS, new AddValue(LevelBasedValue.constant(-0.6F)))
        );

        register(entries, Enchantments.WIND_BURST,
            Enchantment.builder(
                createDefaultProperties(
                    itemLookup.getTagOrThrow(ItemTags.MACE_ENCHANTABLE),
                    1,
                    EquipmentSlotGroup.HAND
                )
            )
            .withExclusiveSet(A_LEVEL)
            .addEffect(
                EnchantmentEffectComponentTypes.POST_ATTACK,
                EnchantmentEffectTarget.ATTACKER,
                EnchantmentEffectTarget.ATTACKER,
                new Explode(
                    false,
                    Optional.empty(),
                    Optional.of(LevelBasedValue.lookup(List.of(1.2F, 1.75F, 2.2F), LevelBasedValue.linear(1.5F, 0.35F))),
                    registries.getLookupOrThrow(RegistryKeys.BLOCK).getTag(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()),
                    Vec3d.ZERO,
                    LevelBasedValue.constant(3.5F),
                    false,
                    World.ExplosionSourceType.TRIGGER,
                    ParticleTypes.GUST_EMITTER_SMALL,
                    ParticleTypes.GUST_EMITTER_LARGE,
                    SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST
                ),
                EntityPropertiesLootCondition.builder(
                    LootContext.EntityTarget.DIRECT_ATTACKER,
                    EntityPredicate.Builder.create()
                        .flags(EntityFlagsPredicate.Builder.create().method_59919(false))
                        .method_59930(C_rubkprmd.method_60282(NumberRange.FloatRange.atLeast(1.5)))
                )
            )
        );
    }
}
