package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.enchantments.effects.lumberjack.LumberjackEvent;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.SmolderingComponent;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.HolderLookup.RegistryLookup;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	private static final int MAX_HEALTH = 80;

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

	@Inject(method = "getAirSpeed", at = @At("HEAD"), cancellable = true)
	protected void getAirSpeed(CallbackInfoReturnable<Float> cir)
	{
		PlayerEntity e = ((PlayerEntity)((Object)this));
		RegistryLookup<Enchantment> lookup = e.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT);
		int level = EnchantmentHelper.getHighestEquippedLevel(lookup.getHolderOrThrow(ModEnchantments.AERODYNAMIC), e);

		if (level > 0)
		{
			cir.setReturnValue((this.isSprinting() ? 0.025999999F : 0.02F) * (1 + ((float)level * 1.8f)));
			cir.cancel();
		}
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		if (source.isTypeIn(DamageTypeTags.IS_FIRE))
		{
			SmolderingComponent smoldering = ModComponentKeys.SMOLDERING.get(this);

			if (smoldering.hasEnchantment())
			{
				final float chargeAmount = amount * 0.25f;

				if (smoldering.isServer())
				{
					smoldering.chargeBy(chargeAmount);
				}

				cir.setReturnValue(super.damage(source, chargeAmount));
				cir.cancel();
				return;
			}
		}
	}
}
