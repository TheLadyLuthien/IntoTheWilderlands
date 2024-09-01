package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.skadoosh.minigame.DeathHelper;
import com.skadoosh.minigame.TeamRefrence;
import com.skadoosh.minigame.ZoneHelper;
import com.skadoosh.minigame.persistance.GamePlayerData;
import com.skadoosh.minigame.voicechat.VoicehcatHelper;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin
{
    private static final int MAX_HEALTH = 60;

	@Shadow
	GameProfile gameProfile;

    @Inject(method = "createAttributes", at = @At("HEAD"), cancellable = true)
	private static void createAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> ci) {
		ci.setReturnValue(LivingEntity.createLivingAttributes()
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1F)
			.add(EntityAttributes.GENERIC_ATTACK_SPEED)
			.add(EntityAttributes.GENERIC_LUCK)
			.add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE, 4.5)
			.add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE, 3.0)
			.add(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED)
			.add(EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED)
			.add(EntityAttributes.PLAYER_SNEAKING_SPEED)
			.add(EntityAttributes.PLAYER_MINING_EFFICIENCY)
			.add(EntityAttributes.PLAYER_SWEEPING_DAMAGE_RATIO)
            .add(EntityAttributes.GENERIC_MAX_HEALTH, PlayerEntityMixin.MAX_HEALTH)
        );
        ci.cancel();
	}

	@Inject(method = "getName", at = @At("HEAD"), cancellable = true)
	private void getName(CallbackInfoReturnable<Text> ci)
	{
		PlayerEntity pe = (PlayerEntity)((Object)this);
		ci.setReturnValue(Text.literal(this.gameProfile.getName()).formatted(DeathHelper.getNameColor(ModComponentKeys.GAME_PLAYER_DATA.get(pe).getLives())));
		ci.cancel();
	}

	@Inject(method = "tickMovement", at = @At("TAIL"))
	public void tickMovement(CallbackInfo ci) 
	{
		PlayerEntity te = (PlayerEntity)(Object)(this);
		if (!te.getWorld().isClient)
		{
			ServerPlayerEntity thisEntity = (ServerPlayerEntity)(Object)(this);
			final GamePlayerData gamePlayerData = ModComponentKeys.GAME_PLAYER_DATA.get(thisEntity);
			String previousZoneId = gamePlayerData.getPreviousZoneId();
			TeamRefrence currentZone = ZoneHelper.getZone(thisEntity);
	
			String currentZoneId = currentZone.getId();
	
			if (previousZoneId == null)
			{
				previousZoneId = GamePlayerData.NO_ZONE;
			}
	
			if (currentZoneId == null)
			{
				currentZoneId = GamePlayerData.NO_ZONE;
			}

			if (!currentZoneId.equals(previousZoneId))
			{
				gamePlayerData.setPreviousZoneId(currentZoneId);
	
				thisEntity.sendMessage(ZoneHelper.getZoneType(thisEntity).styleText(Text.literal("You've entered " + currentZone.getName(thisEntity.getWorld()) + " territory")), true);
			
				// join the voice group if needed
				if (currentZone.hasMember(thisEntity))
				{
					VoicehcatHelper.joinTeamGroup(thisEntity);
				}
			}
		}
	}
}
