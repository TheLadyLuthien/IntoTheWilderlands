package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.skadoosh.wilderlands.enchantments.effects.lumberjack.LumberjackEvent;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	private static final int MAX_HEALTH = 60;

	@Inject(method = "createAttributes", at = @At("HEAD"), cancellable = true)
	private static void createAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> ci)
	{
		ci.setReturnValue(LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1F).add(EntityAttributes.GENERIC_ATTACK_SPEED).add(EntityAttributes.GENERIC_LUCK)
				.add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE, 4.5).add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, 3.0).add(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED).add(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED)
				.add(EntityAttributes.PLAYER_SNEAKING_SPEED).add(EntityAttributes.PLAYER_MINING_EFFICIENCY).add(EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO).add(EntityAttributes.GENERIC_MAX_HEALTH, PlayerEntityMixin.MAX_HEALTH));
		ci.cancel();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$lumberjack(CallbackInfo ci)
	{
		if (!handSwinging)
		{
			LumberjackEvent.ENTRIES.removeIf(entry -> entry.player() == (Object)this);
		}
	}
}
