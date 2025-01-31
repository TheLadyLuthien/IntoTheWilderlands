package com.skadoosh.wilderlands.misc;

import com.mojang.serialization.MapCodec;
import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModLootConditionTypes
{
    public static final LootConditionType BLOCK_TAG = register("block_tag", BlockTagLootCondition.CODEC);

    private static LootConditionType register(String id, MapCodec<? extends LootCondition> codec)
    {
        return Registry.register(Registries.LOOT_CONDITION_TYPE, Wilderlands.id(id), new LootConditionType(codec));
    }

    public static void init()
    {
        
    }
}
