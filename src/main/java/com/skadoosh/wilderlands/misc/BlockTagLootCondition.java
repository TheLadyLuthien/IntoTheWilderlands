package com.skadoosh.wilderlands.misc;

import java.util.Set;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public record BlockTagLootCondition(TagKey<Block> tag) implements LootCondition
{
	public static final MapCodec<BlockTagLootCondition> CODEC = RecordCodecBuilder.<BlockTagLootCondition>mapCodec(
			instance -> instance.group(TagKey.createCodec(RegistryKeys.BLOCK).fieldOf("tag").forGetter(BlockTagLootCondition::tag)).apply(instance,
					BlockTagLootCondition::new));

	// private static DataResult<BlockTagLootCondition> validate(BlockTagLootCondition lootConditions)
	// {
	// 	return lootConditions.properties().flatMap(statePredicate -> statePredicate.method_53235(lootConditions.block().value().getStateManager()))
	// 			.map(string -> DataResult.error(() -> "Block " + lootConditions.block() + " has no property" + string)).orElse(DataResult.success(lootConditions));
	// }

	@Override
	public LootConditionType getType()
	{
		return LootConditionTypes.BLOCK_STATE_PROPERTY;
	}

	@Override
	public Set<LootContextParameter<?>> getRequiredParameters()
	{
		return Set.of(LootContextParameters.BLOCK_STATE);
	}

	public boolean test(LootContext lootContext)
	{
		BlockState blockState = lootContext.get(LootContextParameters.BLOCK_STATE);
		return blockState != null && blockState.isIn(this.tag);
	}

	// public static BlockTagLootCondition.Builder builder(Block block)
	// {
	// 	return new BlockTagLootCondition.Builder(block);
	// }

	// public static class Builder implements LootCondition.Builder
	// {
	// 	private final TagKey<Block> tag;

	// 	public Builder(TagKey<Block> block)
	// 	{
	// 		this.tag = block;
	// 	}

	// 	@Override
	// 	public LootCondition build()
	// 	{
	// 		return new BlockTagLootCondition(this.tag);
	// 	}
	// }
}
