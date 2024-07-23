package com.skadoosh.wilderlands.datagen;

import java.lang.reflect.Field;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.items.ModItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.model.BlockStateModelGenerator;
import net.minecraft.data.client.model.BlockStateVariant;
import net.minecraft.data.client.model.Models;
import net.minecraft.data.client.model.MultipartBlockStateSupplier;
import net.minecraft.data.client.model.VariantSettings;
import net.minecraft.data.client.model.When;
import net.minecraft.item.Item;

public class ModelGenerator extends FabricModelProvider
{
    public ModelGenerator(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator)
    {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator)
    {
        Class<ModItems> itemsClass = ModItems.class;
        for (Field field : itemsClass.getFields())
        {
            if (field.isAnnotationPresent(GenerateItemModel.class))
            {
                try
                {
                    Object object = field.get(null);
                    if (object instanceof Block)
                    {
                        generateItemModel(ModItems.BLOCK_ITEMS.get(object), itemModelGenerator);
                    }
                    else if (object instanceof Item)
                    {
                        generateItemModel((Item)object, itemModelGenerator);
                    }
                }
                catch (IllegalArgumentException e)
                {
                    Wilderlands.LOGGER.error("@GenerateItemModel failed. Field is not static", e);
                }
                catch (IllegalAccessException e)
                {
                    Wilderlands.LOGGER.error("@GenerateItemModel failed. Field is not public", e);
                }
            }
        }
    }

    private void generateItemModel(Item item, ItemModelGenerator generator)
    {
        generator.register(item, Models.SINGLE_LAYER_ITEM);
    }
}
