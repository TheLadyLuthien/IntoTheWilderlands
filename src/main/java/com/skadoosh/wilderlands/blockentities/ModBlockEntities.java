package com.skadoosh.wilderlands.blockentities;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blocks.ModBlocks;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModBlockEntities
{
    public static final BlockEntityType<CarvedRunestoneBlockEntity> CARVED_RUNESTONE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Wilderlands.id("carved_runestone_block_entity"), BlockEntityType.Builder.create(CarvedRunestoneBlockEntity::new, ModBlocks.CARVED_RUNESTONE).build());
    
    public static final BlockEntityType<DeployerBlockEntity> DEPLOYER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Wilderlands.id("deployer"), BlockEntityType.Builder.create(DeployerBlockEntity::new, ModBlocks.DEPLOYER).build());

    public static void init()
    {
        // juszt load the file
    }
}
