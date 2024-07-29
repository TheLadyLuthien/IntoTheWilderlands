package com.skadoosh.wilderlands.items.itemGroup;

import java.util.ArrayList;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.datagen.AutoTranslate;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.misc.AnnotationHelper;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ModItemGroups
{
    @AutoTranslate("Bifrost, The Rainbow Bridge")
    public static final String BIFROST = "birfost";
    
    public static void register()
    {
        register(BIFROST, ModItems.BIFROST_KEY);
    }
    
    private static void register(String id, Item icon)
    {
        ItemGroup itemGroup = FabricItemGroup.builder().icon(() -> new ItemStack(icon)).name(Text.translatable(TRANSLATION_KEY_STUB + id)).entries((context, entries) -> {
            ArrayList<AnnotationHelper.ValueAnnotationPair<Item, AutoItemGroup>> items = AnnotationHelper.getFieldsWithAnnotation(AutoItemGroup.class, ModItems.class, Item.class);
            for (var itemData : items)
            {
                if (itemData.annotation.value().equals(id))
                {
                    entries.addItem(itemData.value);
                }
            }
            ArrayList<AnnotationHelper.ValueAnnotationPair<Block, AutoItemGroup>> blocks = AnnotationHelper.getFieldsWithAnnotation(AutoItemGroup.class, ModBlocks.class, Block.class);
            for (var blockData : blocks)
            {
                if (blockData.annotation.value().equals(id))
                {
                    Item item = ModItems.BLOCK_ITEMS.get(blockData.value);
                    entries.addItem(item);
                }
            }
        }).build();
        Registry.register(Registries.ITEM_GROUP, Wilderlands.id(id), itemGroup);
    }

    public static final String TRANSLATION_KEY_STUB = "itemGroup.wilderlands.";
}
