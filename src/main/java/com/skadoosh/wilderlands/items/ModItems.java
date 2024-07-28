package com.skadoosh.wilderlands.items;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.datagen.GenerateItemModel;

import net.minecraft.block.Block;
import net.minecraft.component.DataComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public final class ModItems
{
    public static final HashMap<Block, Item> BLOCK_ITEMS = new HashMap<>();

    @GenerateItemModel
    public static final Item BIFROST_KEY = register("bifrost_key", new Item(new Item.Settings().rarity(Rarity.RARE).component(ModComponents.BIFROST_KEY, NbtComponent.of(new NbtCompound()))));

    public static void init()
    {
        // TODO: fill block items map
    }

    private static Item register(String name, Item item)
    {
        return Registry.register(Registries.ITEM, Wilderlands.id(name), item);
    }
}
