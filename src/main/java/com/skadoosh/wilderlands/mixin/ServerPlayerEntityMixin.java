package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.skadoosh.minigame.DeathHelper;
import com.skadoosh.minigame.Minigame;
import com.skadoosh.minigame.ZoneHelper;
import com.skadoosh.minigame.blockentities.GravestoneBlockEntity;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin
{
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo ci)
    {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;

        ServerWorld world = (ServerWorld)(player.getWorld());

        BlockPos blockPos = player.getBlockPos();

        world.setBlockState(blockPos, Minigame.GRAVESTONE_BLOCK.getDefaultState());
        GravestoneBlockEntity gbe = world.getBlockEntity(blockPos, Minigame.GRAVESTONE_BLOCK_ENTITY_TYPE).orElse(null);
        if (gbe != null)
        {
            gbe.setupFromDeath(player.getInventory(), player.getDisplayName().getString());

            if (!DeathHelper.graveIsPublic(player, source))
            {
                final var team = player.getScoreboard().getPlayerTeam(player.getProfileName());
                if (team != null)
                {
                    gbe.setTeam(team.getName());
                }
            }
        }
    }
}
