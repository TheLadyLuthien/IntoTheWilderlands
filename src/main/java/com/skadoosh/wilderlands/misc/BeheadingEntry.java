package com.skadoosh.wilderlands.misc;

import java.util.Map;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public record BeheadingEntry(ItemStack drop, float chance)
{
	public static final Map<EntityType<?>, BeheadingEntry> DROP_MAP = new HashMap<>();
}
