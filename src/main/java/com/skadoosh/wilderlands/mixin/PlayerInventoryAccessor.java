package com.skadoosh.wilderlands.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(PlayerInventory.class)
public interface PlayerInventoryAccessor
{
    @Accessor
    List<DefaultedList<ItemStack>> getCombinedInventory();
}
