package com.skadoosh.wilderlands.misc;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

import org.jetbrains.annotations.Nullable;

import com.skadoosh.wilderlands.blockentities.CarvedRunestoneBlockEntity;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.datagen.tag.ModItemTags;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.misc.AstralForgeEvent.KeyValues;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.NamedKeystoneData;
import com.skadoosh.wilderlands.persistance.NamedKeystoneData.KeystoneLocation;

import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

public final class BifrostHelper
{
    public static final String NBT_TYPE = "type";
    public static final String NBT_USES = "uses_remaining";
    public static final String NBT_DIMENSION = "dimension";
    public static final String NBT_KEYSTONE = "keystone";

    public static enum KeyType {
        TO_SINGLE_DESTINATION, TO_FROM_SINGLE_DESTINATION, TO_SINGLE_DIMENSION, WITHIN_CURRENT_DIMENSION, UNIVERSAL
    }

    public static class ChainBuilder
    {
        private final ItemStack stack;

        protected ChainBuilder(ItemStack stack)
        {
            this.stack = stack;
        }

        public ChainBuilder type(KeyType type)
        {
            setKeyType(stack, type);
            return this;
        }

        public ChainBuilder usesRemaining(int count)
        {
            setKeyUsesesRemaining(stack, count);
            return this;
        }

        public ChainBuilder dimension(World dim)
        {
            setKeyExtra(stack, dim.getRegistryKey().getValue());
            return this;
        }

        public ChainBuilder dimension(String dim)
        {
            setKeyDimension(stack, dim);
            return this;
        }

        public ChainBuilder keystoneData(String name)
        {
            setKeyExtra(stack, desanitizeKeystoneName(name));
            return this;
        }

        public ChainBuilder rawKeystoneData(String id)
        {
            setKeyExtra(stack, id);
            return this;
        }
    }

    public static ChainBuilder buildKey(ItemStack stack)
    {
        clearKeyComponent(stack);
        return new ChainBuilder(stack);
    }

    public static ItemStack getKeyStack(PlayerEntity player)
    {
        final ItemStack mainHand = player.getStackInHand(Hand.MAIN_HAND);
        final ItemStack offhand = player.getStackInHand(Hand.OFF_HAND);

        if (mainHand != null && !mainHand.isEmpty())
        {
            return mainHand;
        }
        else if (offhand != null && !offhand.isEmpty())
        {
            return offhand;
        }
        else
        {
            return null;
        }
    }

    private static NbtCompound getOrCreateKeyComponent(ItemStack stack)
    {
        NbtComponent nbtComponent = stack.get(ModComponents.BIFROST_KEY);
        if (nbtComponent == null)
        {
            return new NbtCompound();
        }
        return nbtComponent.copy();
    }

    public static KeyType getKeyType(NbtCompound nbt)
    {
        try
        {
            return KeyType.valueOf(nbt.getString(NBT_TYPE).toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

    public static NbtCompound getKeyComponent(ItemStack stack)
    {
        NbtComponent nbtComponent = stack.get(ModComponents.BIFROST_KEY);
        if (nbtComponent == null)
        {
            return null;
        }
        return nbtComponent.copy();
    }

    public static Text getTranslatedDimension(NbtCompound nbt)
    {
        String keyName = nbt.getString(NBT_DIMENSION);
        Identifier id = Identifier.parse(keyName);
        return getTranslatedDimension(id);
    }

    public static Text getTranslatedDimension(Identifier id)
    {
        return Text.translatable("bifrost.colorized.dimension." + id.getPath());
    }

    private static void updateStackNbt(ItemStack stack, NbtCompound nbt)
    {
        stack.set(ModComponents.BIFROST_KEY, NbtComponent.of(nbt));
    }

    public static ItemStack clearKeyComponent(ItemStack stack)
    {
        stack.remove(ModComponents.BIFROST_KEY);
        return stack;
    }

    public static ItemStack setKeyType(ItemStack stack, KeyType type)
    {
        NbtCompound nbt = getOrCreateKeyComponent(stack);
        nbt.putString(NBT_TYPE, type.toString().toLowerCase());
        updateStackNbt(stack, nbt);
        return stack;
    }

    public static ItemStack setKeyUsesesRemaining(ItemStack stack, int count)
    {
        NbtCompound nbt = getOrCreateKeyComponent(stack);
        nbt.putInt(NBT_USES, count);
        updateStackNbt(stack, nbt);
        return stack;
    }

    public static ItemStack setKeyExtra(ItemStack stack, Identifier dimension)
    {
        NbtCompound nbt = getOrCreateKeyComponent(stack);
        nbt.putString(NBT_DIMENSION, dimension.toString());
        updateStackNbt(stack, nbt);
        return stack;
    }

    public static ItemStack setKeyDimension(ItemStack stack, String dimension)
    {
        NbtCompound nbt = getOrCreateKeyComponent(stack);
        nbt.putString(NBT_DIMENSION, dimension);
        updateStackNbt(stack, nbt);
        return stack;
    }

    public static ItemStack setKeyExtra(ItemStack stack, String keystoneName)
    {
        NbtCompound nbt = getOrCreateKeyComponent(stack);
        nbt.putString(NBT_KEYSTONE, keystoneName);
        updateStackNbt(stack, nbt);
        return stack;
    }

    private static ItemStack popItemFromStack(ItemStack stack)
    {
        ItemStack newStack = stack.copy();
        stack.setCount(stack.getCount() - 1);
        newStack.setCount(1);
        return newStack;
    }

    private static boolean shouldDeleteKey(ItemStack stack)
    {
        return (stack.getRarity().equals(Rarity.COMMON)) || stack.isOf(ModItems.BIFROST_KEY);
    }

    @Nullable
    public static <T> T pickRandomFromNonNulls(RandomGenerator rand, T a, T b)
    {
        if (a == null)
        {
            return b;
        }
        if (b == null)
        {
            return a;
        }

        // should both not be null by here
        return rand.nextInt(2) == 0 ? a : b;
    }

    @Nullable
    public static KeyType getKeyTypeFromReagent(ItemStack reagent)
    {
        KeyType type = null;

        if (reagent.isIn(ModItemTags.ASTRAL_FORGE_TO_SINGLE_DESTINATION))
        {
            type = KeyType.TO_SINGLE_DESTINATION;
        }

        if (reagent.isIn(ModItemTags.ASTRAL_FORGE_TO_FROM_SINGLE_DESTINATION))
        {
            type = KeyType.TO_FROM_SINGLE_DESTINATION;
        }

        if (reagent.isIn(ModItemTags.ASTRAL_FORGE_TO_SINGLE_DIMENSION))
        {
            type = KeyType.TO_SINGLE_DIMENSION;
        }

        if (reagent.isIn(ModItemTags.ASTRAL_FORGE_WITHIN_CURRENT_DIMENSION))
        {
            type = KeyType.WITHIN_CURRENT_DIMENSION;
        }

        if (reagent.isIn(ModItemTags.ASTRAL_FORGE_UNIVERSAL))
        {
            type = KeyType.UNIVERSAL;
        }

        return type;
    }

    public static void expendKeyIfNecessary(ItemStack stack, PlayerEntity player)
    {
        NbtCompound nbt = getKeyComponent(stack);
        int usesRemaining = nbt.getInt(NBT_USES);
        if (usesRemaining == -1)
        {
            return;
        }
        else
        {
            usesRemaining--;

            if (usesRemaining > 0)
            {
                ItemStack poppedKey = popItemFromStack(stack);
                setKeyUsesesRemaining(poppedKey, usesRemaining);
                player.getInventory().insertStack(poppedKey);
            }
            else
            {
                // destroy item
                // pop item from stack first
                ItemStack poppedKey = popItemFromStack(stack);
                if (shouldDeleteKey(poppedKey))
                {
                    // leave it to the void
                    poppedKey = null;
                }
                else
                {
                    // wipe it and return it to the player
                    clearKeyComponent(poppedKey);
                    player.getInventory().insertStack(poppedKey);
                }
            }
        }
    }

    public static boolean shouldActivateRunestone(ItemStack keyStack, ServerWorld world, BlockPos keystone, CarvedRunestoneBlockEntity runestone)
    {
        final NbtCompound nbt = getOrCreateKeyComponent(keyStack);
        final KeyType type = getKeyType(nbt);

        final NamedKeystoneData nameMap = ModComponentKeys.NAMED_KEYSTONE_DATA.get(world.getServer().getOverworld());

        switch (type)
        {
            case TO_SINGLE_DESTINATION:
            {
                String destinationName = nbt.getString(NBT_KEYSTONE);
                KeystoneLocation destination = nameMap.get(destinationName);

                if (runestone.getDestinationDimension().equals(destination.dimension.toString()) && runestone.getDestinationPos().equals(destination.position))
                {
                    return true;
                }

                break;
            }

            case TO_FROM_SINGLE_DESTINATION:
            {
                String destinationName = nbt.getString(NBT_KEYSTONE);
                KeystoneLocation destination = nameMap.get(destinationName);

                if ((runestone.getDestinationDimension().equals(destination.dimension.toString()) && runestone.getDestinationPos().equals(destination.position))
                        || (world.getRegistryKey().getValue().toString().equals(destination.dimension.toString()) && keystone.equals(destination.position)))
                {
                    return true;
                }

                break;
            }

            case TO_SINGLE_DIMENSION:
            {
                String dimensionName = nbt.getString(NBT_DIMENSION);

                if (runestone.getDestinationDimension().equals(dimensionName))
                {
                    return true;
                }

                break;
            }

            case WITHIN_CURRENT_DIMENSION:
            {
                if (world.getRegistryKey().getValue().toString().equals(runestone.getDestinationDimension()))
                {
                    return true;
                }
                break;
            }

            case UNIVERSAL:
                return true;

            default:
                return false;
        }
        return false;
    }

    public static String desanitizeKeystoneName(String name)
    {
        return name.replaceAll(Matcher.quoteReplacement("$"), "§");
    }

    public static String sanitizeKeystoneName(String name)
    {
        return name.replaceAll("§", Matcher.quoteReplacement("$"));
    }

    public static <E> E getRandomSetElement(Collection<E> set, RandomGenerator random)
    {
        return set.stream().skip(random.nextInt(set.size())).findFirst().orElse(null);
    }

    public static KeystoneLocation getRandomKeystone(World world)
    {
        final var map = ModComponentKeys.NAMED_KEYSTONE_DATA.get(world.getServer().getOverworld()).map;

        return getRandomSetElement(map.keySet(), world.getRandom());
    }
    public static String getRandomKeystoneName(World world)
    {
        final var map = ModComponentKeys.NAMED_KEYSTONE_DATA.get(world.getServer().getOverworld()).map;

        return getRandomSetElement(map.values(), world.getRandom());
    }

    public static record KeyForgingResult(ItemStack stack, int rollResult) {}

    public static KeyForgingResult processKeyForging(World world, ItemStack baseStack, ItemStack keyStack, final ItemStack reagent1Stack, final ItemStack reagent2Stack)
    {
        final ItemStack result = baseStack.copy();

        NbtCompound baseKey = getOrCreateKeyComponent(baseStack);

        KeyType baseType = getKeyType(baseKey);
        int baseUsesRemaining = baseKey.getInt(NBT_USES);
        String baseDimension = baseKey.getString(NBT_DIMENSION);
        String baseFixedDestination = baseKey.getString(NBT_KEYSTONE);
        
        if (baseType == null)
        {
            baseType = KeyType.TO_SINGLE_DESTINATION;
        }
  
        if (keyStack != null && !keyStack.isEmpty())
        {
            NbtCompound key = getOrCreateKeyComponent(keyStack);
    
            final KeyType keyType = getKeyType(key);
            final int keyUsesRemaining = key.getInt(NBT_USES);
            final String keyDimension = key.getString(NBT_DIMENSION);
            final String keyFixedDestination = key.getString(NBT_KEYSTONE);
            
            // add uses toghether (unless it is unlimited)
            if ((baseUsesRemaining == -1) || (keyUsesRemaining == -1))
            {
                baseUsesRemaining = -1;
            }
            else
            {
                // in ordinal adjusted value units
                int base = baseUsesRemaining * (baseType.ordinal() + 1);
                int template = keyUsesRemaining * (keyType.ordinal() + 1);
    
                // devide by final ordinal later
                baseUsesRemaining = base + template;
            }

            // if template key is better, update the base key
            if (keyType.ordinal() > baseType.ordinal())
            {
                baseType = keyType;
            }
    
            // override base dimension to template dimension, or overworld
            if (keyDimension.isEmpty())
            {
                // default to overworld if no dimensions in either
                baseDimension = "minecraft:overworld";
            }
            else
            {
                baseDimension = keyDimension;
            }

            // override base fixedDestination to template one, or random
            if (keyFixedDestination.isEmpty())
            {
                // default to random if no dimensions in either
                final String keystone = getRandomKeystoneName(world);
                baseFixedDestination = keystone;
            }
            else
            {
                baseFixedDestination = keyFixedDestination;
            }
        }
        else
        {
            if (baseDimension.isEmpty())
            {
                baseDimension = "minecraft:overworld";
            }
            if (baseUsesRemaining == 0)
            {
                baseUsesRemaining = 1;
            }
            if (baseFixedDestination.isEmpty())
            {
                baseFixedDestination = getRandomKeystoneName(world);
            }

            baseUsesRemaining = baseUsesRemaining * (baseType.ordinal() + 1);
        }

        // Process Reagents
        final KeyType reagent1KeyTypeOverride = (reagent1Stack != null && !reagent1Stack.isEmpty()) ? getKeyTypeFromReagent(reagent1Stack) : null;
        final KeyType reagent2KeyTypeOverride = (reagent2Stack != null && !reagent2Stack.isEmpty()) ? getKeyTypeFromReagent(reagent2Stack) : null;

        @Nullable
        final KeyType overrideKeyType = pickRandomFromNonNulls(world.getRandom(), reagent1KeyTypeOverride, reagent2KeyTypeOverride);

        if (overrideKeyType != null)
        {
            baseType = overrideKeyType;
        }

        final int mishapScore = 10;
        final int startingReagentScore = 20;
        int reagentScore = startingReagentScore;

        // base is 20
        // max rarity is 4, so max from rarity is 32
        // enchantments add 4, so max from from enchantments is 8
        // using keys add 1, so max from from keys is 2
        // total possible value is 62
        for (ItemStack stack : List.of(reagent1Stack, reagent2Stack))
        {
            if (stack == null)
            {
                continue;
            }

            reagentScore += MathHelper.square(stack.getRarity().ordinal() + 1);
            if (stack.hasEnchantments())
            {
                reagentScore += 4;
            }

            if (stack.isOf(ModItems.BIFROST_KEY))
            {
                reagentScore += 1;
            }
        }

        // do the roll!
        final int forgeRoll = world.getRandom().nextInt(reagentScore + mishapScore) - mishapScore;

        final KeyValues keyValues = new KeyValues(baseType, baseUsesRemaining, baseDimension, baseFixedDestination);
        if (forgeRoll > 0)
        {
            // sucess, upgrade
            // but only upgrade if it was from the reagents
            if (forgeRoll > startingReagentScore)
            {
                AstralForgeEvent.apply(keyValues, forgeRoll, AstralForgeEvent.UPGRADE_EVENTS);
            }
        }
        else
        {
            // mishap, downgrade
            AstralForgeEvent.apply(keyValues, -forgeRoll, AstralForgeEvent.MISHAP_EVENTS);
        }

        baseType = keyValues.type;
        baseUsesRemaining = keyValues.usesRemaining;
        baseDimension = keyValues.dimension;
        baseFixedDestination = keyValues.fixedDestination;

        // build the final key
        // convert to uses from ordinal adjusted use units
        baseUsesRemaining /= (baseType.ordinal() + 1);

        // min 1 use
        baseUsesRemaining = Math.max(baseUsesRemaining, 1);

        buildKey(result)
        .type(baseType)
        .usesRemaining(baseUsesRemaining)
        .dimension(baseDimension)
        .rawKeystoneData(baseFixedDestination);

        return new KeyForgingResult(result, forgeRoll);
    }

    public static void showTitleToPlayer(ServerPlayerEntity player, String keystoneId)
    {
        player.networkHandler.send(new SubtitleS2CPacket(Text.literal(keystoneId)));
        player.networkHandler.send(new TitleS2CPacket(Text.literal("")));
    }
}
