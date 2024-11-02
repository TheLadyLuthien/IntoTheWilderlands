package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import org.ladysnake.cca.api.v3.entity.C2SSelfMessagingComponent;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DashComponent implements C2SSelfMessagingComponent, /* AutoSyncedComponent, */ ClientTickingComponent, ServerTickingComponent
{
    public static final int COOLDOWN = 15;
    public static final int FALL_DAMAGE_BUFFER = 10;
    public static final float LAUNCH_STRENGTH = 1.9f;

    private final PlayerEntity player;

    private int cooldown = 0;
    public int getCooldown()
    {
        return cooldown;
    }

    private int dashesRemaining = 0;

    public int getDashesRemaining()
    {
        return dashesRemaining;
    }

    public DashComponent(PlayerEntity player)
    {
        this.player = player;
    }

    public boolean canUse()
    {
        return (cooldown <= 0) &&
        (dashesRemaining > 0) &&
        !(player.isOnGround() || player.isClimbing()) &&
        !player.isFallFlying() &&
        !player.hasVehicle() &&
        !player.isTouchingWater() &&
        (!player.getAbilities().flying);
    }


    public boolean shouldRenderIcon()
    {
        return !(player.isOnGround() || player.isClimbing()) &&
        !player.isFallFlying() &&
        !player.hasVehicle() &&
        !player.getAbilities().flying &&
        (dashesRemaining > 0);
    }

    public boolean hasEnchantment()
    {
        return getEnchantmentLevel() > 0;
    }
    
    public int getEnchantmentLevel()
    {
        return EnchantmentHelper.getHighestEquippedLevel(player.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.DASH), player);
    }

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        this.cooldown = tag.getInt("cooldown");
        this.dashesRemaining = tag.getInt("dashes_remaining");
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        tag.putInt("cooldown", cooldown);
        tag.putInt("dashes_remaining", dashesRemaining);
    }

    @Override
    public void clientTick()
    {
        if (player.isOnGround() || player.isClimbing())
        {
            // cooldown = 0;
            dashesRemaining = getEnchantmentLevel();
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
            var vec = player.getRotationVector().normalize();
            player.setVelocity(vec.x * LAUNCH_STRENGTH, vec.y * LAUNCH_STRENGTH, vec.z * LAUNCH_STRENGTH);
            
            sendC2SMessage(buf -> {});
            
            player.velocityDirty = true;
            dashesRemaining--;
            cooldown = COOLDOWN;
        }
    }
    
    @Override
    public void handleC2SMessage(RegistryByteBuf buf)
    {
        player.resetFallDistance();
        cooldown = COOLDOWN;
        ((ServerWorld)(player.getWorld())).spawnParticles(ParticleTypes.GUST, player.getX(), player.getY(), player.getZ(), dashesRemaining, 1.0f, 1.0f, 1.0f, 1.25f);
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
