package com.skadoosh.wilderlands.items;

import com.skadoosh.wilderlands.items.crossbow.ArrowCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.FireworkCrossbowProjectileBehavior;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CrossbowProjectileBehaviors
{
    private static void register(Item item, CrossbowProjectileBehavior behavior)
    {
        CrossbowProjectileBehavior.register(item, behavior);
    }

    public static void init()
    {
        register(Items.ARROW, new ArrowCrossbowProjectileBehavior());
        register(Items.TIPPED_ARROW, new ArrowCrossbowProjectileBehavior());
        register(Items.SPECTRAL_ARROW, new ArrowCrossbowProjectileBehavior());
        register(Items.FIREWORK_ROCKET, new FireworkCrossbowProjectileBehavior());
    }
}
