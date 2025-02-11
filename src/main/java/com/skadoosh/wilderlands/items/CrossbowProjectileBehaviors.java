package com.skadoosh.wilderlands.items;

import com.skadoosh.wilderlands.items.crossbow.ArrowCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.BrimstoneCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.BundleItemCrossbowBehavior;
import com.skadoosh.wilderlands.items.crossbow.DragonFireballCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.EnderPearlCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.FireballCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.FireworkCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.PotionCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.RecoilCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.SonicBoomCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.TorchCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.items.crossbow.WitherSkullCrossbowProjectileBehavior;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CrossbowProjectileBehaviors
{
    private static final BundleItemCrossbowBehavior BUNDLE_BEJAVIOR = new BundleItemCrossbowBehavior();
    
    private static void register(Item item, CrossbowProjectileBehavior behavior)
    {
        CrossbowProjectileBehavior.register(item, behavior);
    }

    public static void init()
    {
        register(Items.ARROW, new ArrowCrossbowProjectileBehavior());
        register(Items.TIPPED_ARROW, new ArrowCrossbowProjectileBehavior());
        register(Items.SPECTRAL_ARROW, new  ArrowCrossbowProjectileBehavior());
        register(Items.FIREWORK_ROCKET, new FireworkCrossbowProjectileBehavior());
        register(Items.BLAZE_ROD, new FireballCrossbowProjectileBehavior());
        register(Items.ENDER_PEARL, new EnderPearlCrossbowProjectileBehavior());
        register(Items.ECHO_SHARD, new SonicBoomCrossbowProjectileBehavior());
        register(Items.SPLASH_POTION, new PotionCrossbowProjectileBehavior());
        register(Items.LINGERING_POTION, new PotionCrossbowProjectileBehavior());
        register(Items.DRAGON_BREATH, new DragonFireballCrossbowProjectileBehavior());
        register(Items.BREEZE_ROD, new RecoilCrossbowProjectileBehavior(3.0f));
        register(Items.TORCH, new TorchCrossbowProjectileBehavior());
        register(Items.WITHER_SKELETON_SKULL, new WitherSkullCrossbowProjectileBehavior());
        register(ModItems.BRIMSTONE_CARTRIDGE, new BrimstoneCrossbowProjectileBehavior());
        register(Items.BUNDLE, BUNDLE_BEJAVIOR);
    }
}
