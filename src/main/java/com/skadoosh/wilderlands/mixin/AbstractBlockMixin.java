package com.skadoosh.wilderlands.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.skadoosh.wilderlands.datagen.tag.ModBlockTags;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.enchantments.effects.lumberjack.LumberjackEvent;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.HolderLookup.RegistryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin
{
    @ModifyReturnValue(method = "calcBlockBreakingDelta", at = @At("RETURN"))
    private float enchancement$lumberjack(float original, BlockState state, PlayerEntity player, BlockView world, BlockPos pos)
    {
        TagKey<Block> tag = null;
        RegistryLookup<Enchantment> lookup = player.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT);
        if (EnchantmentHelper.getLevel(lookup.getHolderOrThrow(ModEnchantments.PROSPECTOR), player.getMainHandStack()) > 0)
        {
            tag = ModBlockTags.ORES;
        }
        else
        {
            tag = BlockTags.LOGS;
        }

        if (LumberjackEvent.canActivate(player, player.getMainHandStack(), state))
        {
            LumberjackEvent.Entry entry = LumberjackEvent.Entry.get(player);
            if (entry == null)
            {
                entry = new LumberjackEvent.Entry(player, LumberjackEvent.gatherTree(new ArrayList<>(), world, new BlockPos.Mutable().set(pos), state.getBlock(), tag));
                LumberjackEvent.ENTRIES.add(entry);
            }
            if (LumberjackEvent.isValid(entry.tree()))
            {
                return original * MathHelper.lerp(Math.min(1, entry.tree().size() / 24F), 1, 0.1F);
            }
        }
        return original;
    }
}
