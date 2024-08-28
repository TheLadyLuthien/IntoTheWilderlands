package com.skadoosh.wilderlands.events;

import com.skadoosh.minigame.DeathHelper;
import com.skadoosh.minigame.GamePlayerData;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents.AfterKilledOtherEntity;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.StopSleeping;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public final class ModEvents
{
    public static void register()
    {
        EntitySleepEvents.STOP_SLEEPING.register(new StopSleeping() {
            @Override
            public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos)
            {
                entity.heal(entity.getMaxHealth());
            }
        });

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new AfterKilledOtherEntity() {

            @Override
            public void afterKilledOtherEntity(ServerWorld world, Entity killer, LivingEntity killedEntity)
            {
                if (killer instanceof ServerPlayerEntity && killedEntity instanceof ServerPlayerEntity)
                {
                    final ServerPlayerEntity killerPlayer = (ServerPlayerEntity)killer;
                    final ServerPlayerEntity deadPlayer = (ServerPlayerEntity)killedEntity;
                    

                }
            }
        });

        // ServerPlayerEvents.AFTER_RESPAWN.register(new AfterRespawn() {

        //     @Override
        //     public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive)
        //     {
        //         if (!alive)
        //         {

        //         }
        //     }
            
        // });

        ServerLivingEntityEvents.AFTER_DEATH.register(new AfterDeath() {

            @Override
            public void afterDeath(LivingEntity entity, DamageSource damageSource)
            {
                if (entity instanceof ServerPlayerEntity && !entity.getWorld().isClient)
                {
                    final ServerPlayerEntity player = (ServerPlayerEntity)entity;
                    if (DeathHelper.shouldMarkDeath(player, damageSource))
                    {
                        GamePlayerData gamePlayerData = ModComponentKeys.GAME_PLAYER_DATA.get(player);
                        gamePlayerData.onMarkedDeath();
                    }
                }
            }
        });
        //  .STOP_SLEEPING.register(new StopSleeping() {
        //     @Override
        //     public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos)
        //     {
        //         entity.heal(entity.getMaxHealth());
        //     }
        // });

        
    }
}
