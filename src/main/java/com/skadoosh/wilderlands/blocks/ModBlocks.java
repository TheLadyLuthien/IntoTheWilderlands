package com.skadoosh.wilderlands.blocks;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.datagen.GenerateItemModel;
import com.skadoosh.wilderlands.items.ModItems;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModBlocks
{
    public static final Block CARVED_RUNESTONE = register("carved_runestone", new CarvedRunestoneBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).offsetType(AbstractBlock.OffsetType.XZ).dynamicBounds()), true);
    
    public static final Block RUNIC_KEYSTONE = register("runic_keystone", new RunicKeystoneBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK)), true);
    
    @GenerateItemModel
    public static final Block DEPLOYER = register("deployer", new DeployerBlock(AbstractBlock.Settings.copy(Blocks.DISPENSER).pistonBehavior(PistonBehavior.NORMAL)), true);
    
    @GenerateItemModel
    public static final Block DESTROYER = register("destroyer", new DestroyerBlock(AbstractBlock.Settings.copy(Blocks.DISPENSER).pistonBehavior(PistonBehavior.NORMAL)), true);

    private static Block register(String name, Block block, boolean shouldMakeItem)
    {
        Identifier identifier = Wilderlands.id(name);

        if (shouldMakeItem)
        {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, identifier, blockItem);
			ModItems.BLOCK_ITEMS.put(block, blockItem);
        }

        return Registry.register(Registries.BLOCK, identifier, block);
    }

    public static void init()
    {
    }
}
