package com.skadoosh.wilderlands.blocks;

import com.skadoosh.mcutils.datagen.AutoTranslate;
import com.skadoosh.mcutils.datagen.GenerateItemModel;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.items.itemGroup.AutoItemGroup;
import com.skadoosh.wilderlands.items.itemGroup.ModItemGroups;

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
    @AutoTranslate("Carved Runestone")
    @AutoItemGroup(ModItemGroups.BIFROST)
    public static final Block CARVED_RUNESTONE = register("carved_runestone", new CarvedRunestoneBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK).noCollision().offsetType(AbstractBlock.OffsetType.XZ)), true);
    
    @GenerateItemModel
    @AutoTranslate("Runic Keystone")
    @AutoItemGroup(ModItemGroups.BIFROST)
    public static final Block RUNIC_KEYSTONE = register("runic_keystone", new RunicKeystoneBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK)), true);
    
    @GenerateItemModel
    @AutoTranslate("Deployer")
    public static final Block DEPLOYER = register("deployer", new DeployerBlock(AbstractBlock.Settings.copy(Blocks.DISPENSER).pistonBehavior(PistonBehavior.NORMAL)), true);
    
    @GenerateItemModel
    @AutoTranslate("Destroyer")
    public static final Block DESTROYER = register("destroyer", new DestroyerBlock(AbstractBlock.Settings.copy(Blocks.DISPENSER).pistonBehavior(PistonBehavior.NORMAL)), true);
    
    @GenerateItemModel
    @AutoTranslate("Holotile")
    public static final Block HOLOTILE = register("holotile", new HolotileBlock(AbstractBlock.Settings.copy(Blocks.TINTED_GLASS)), true);
    
    @GenerateItemModel
    @AutoTranslate("Astral Forge")
    @AutoItemGroup(ModItemGroups.BIFROST)
    public static final Block ASTRAL_FORGE_CORE = register("astral_forge_core", new AstralForgeCoreBlock(AbstractBlock.Settings.copy(Blocks.SMITHING_TABLE)), true);

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
