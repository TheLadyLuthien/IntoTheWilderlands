package com.skadoosh.wilderlands.misc;

import java.util.regex.Matcher;

import com.skadoosh.wilderlands.blockentities.CarvedRunestoneBlockEntity;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.NamedKeystoneData;
import com.skadoosh.wilderlands.persistance.NamedKeystoneData.KeystoneLocation;

import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
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

        public ChainBuilder keystoneData(String name)
        {
            setKeyExtra(stack, name.replaceAll(Matcher.quoteReplacement("$"), "§"));
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
}
