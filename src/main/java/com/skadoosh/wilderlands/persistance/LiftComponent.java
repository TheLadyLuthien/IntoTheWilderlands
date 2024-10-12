package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import org.ladysnake.cca.api.v3.entity.C2SSelfMessagingComponent;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.HolderLookup.Provider;

public class LiftComponent implements C2SSelfMessagingComponent, /* AutoSyncedComponent, */ ServerTickingComponent, ClientTickingComponent
{
    public static final int COOLDOWN = 5;
    public static final int FALL_DAMAGE_BUFFER = 15;
    public static final float JUMP_STRENGTH = 0.52f;


    private final PlayerEntity player;

    private int cooldown = 0;
    public int getCooldown()
    {
        return cooldown;
    }

    private int jumpsRemaining = 0;

    public int getJumpsRemaining()
    {
        return jumpsRemaining;
    }

    public LiftComponent(PlayerEntity player)
    {
        this.player = player;
    }

    public boolean canUse()
    {
        return (cooldown <= 0) &&
        (jumpsRemaining > 0) &&
        !(player.isOnGround() || player.isClimbing()) &&
        !player.isFallFlying() &&
        !player.hasVehicle() &&
        !player.isTouchingWater() &&
        !player.hasStatusEffect(StatusEffects.LEVITATION) &&
        (!player.getAbilities().flying);
    }
    
    public boolean shouldRenderIcon()
    {
        return !(player.isOnGround() || player.isClimbing()) &&
        !player.isFallFlying() &&
        !player.hasVehicle() &&
        !player.hasStatusEffect(StatusEffects.LEVITATION) &&
        !player.getAbilities().flying &&
        (jumpsRemaining > 0);
    }

    public boolean hasEnchantment()
    {
        return getEnchantmentLevel() > 0;
    }
    
    public int getEnchantmentLevel()
    {
        return EnchantmentHelper.getHighestEquippedLevel(player.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.LIFT), player);
    }

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        this.cooldown = tag.getInt("cooldown");
        this.jumpsRemaining = tag.getInt("jumps_remaining");
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        tag.putInt("cooldown", cooldown);
        tag.putInt("jumps_remaining", jumpsRemaining);
    }

    @Override
    public void clientTick()
    {
        if (player.isOnGround() || player.isClimbing())
        {
            // cooldown = 0;
            jumpsRemaining = getEnchantmentLevel();
        }
        // else
        // {
        // }
        if (cooldown > 0)
        {
            cooldown--;
        }
    }

    public void trigger()
    {
        if (canUse())
        {
            Vec3d vec3d = player.getVelocity();
            player.setVelocity(vec3d.x, (double)JUMP_STRENGTH, vec3d.z);
            if (player.isSprinting())
            {
                float g = player.getYaw() * (float)(Math.PI / 180.0);
                player.addVelocity(new Vec3d((double)(-MathHelper.sin(g)) * 0.2, 0.0, (double)MathHelper.cos(g) * 0.2));
            }
    
            
            sendC2SMessage(buf -> {});
            
            player.velocityDirty = true;
            jumpsRemaining--;
            cooldown = COOLDOWN;
        }
    }
    
    @Override
    public void handleC2SMessage(RegistryByteBuf buf)
    {
        player.resetFallDistance();
        cooldown = COOLDOWN;
        ((ServerWorld)(player.getWorld())).spawnParticles(ParticleTypes.GUST, player.getX(), player.getY(), player.getZ(), jumpsRemaining, 1.0f, 1.0f, 1.0f, 1.25f);
    }

    @Override
    public void serverTick()
    {
        if (cooldown > -FALL_DAMAGE_BUFFER)
        {
            cooldown--;
        }
    }

    public boolean isFalldamageImmune()
    {
        return cooldown > -FALL_DAMAGE_BUFFER;
    }
}
