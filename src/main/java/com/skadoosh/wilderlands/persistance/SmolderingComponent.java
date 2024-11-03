package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.registry.RegistryKeys;

public class SmolderingComponent implements ServerTickingComponent, AutoSyncedComponent, ClientTickingComponent
{
    public SmolderingComponent(PlayerEntity player)
    {
        this.player = player;
    }

    private static final float PARTICLE_RANGE = 0.5f;
    private static final int MIN_PARTICLE_DELAY = 2;
    private static final int MAX_PARTICLE_DELAY = 20;
    private static final int MIN_PARTICLE_COUNT = 2;
    private static final int MAX_PARTICLE_COUNT = 10;
    private static final float MAX_CHARGE = 20f;

    private final PlayerEntity player;
    private float charge = 0;

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        charge = tag.getFloat("charge");
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        tag.putFloat("charge", charge);
    }

    @Override
    public void serverTick()
    {
        tick++;
        if (player.isTouchingWater() && charge > 0)
        {
            quench();
        }
    }

    int tick = 0;

    @Override
    public void clientTick()
    {
        tick++;

        if (charge > 0)
        {
            final int rate = (int)((float)MIN_PARTICLE_DELAY + ((1 - getChargePrecent()) * ((float)MAX_PARTICLE_DELAY - (float)MIN_PARTICLE_DELAY)));
            if (tick % rate == 0)
            {
                final int count = (int)((float)MIN_PARTICLE_COUNT + ((getChargePrecent()) * ((float)MAX_PARTICLE_COUNT - (float)MIN_PARTICLE_COUNT)));
                for (int index = 0; index < count; index++)
                {
                    spawnParticles();
                }
            }
        }
    }

    private void spawnParticles()
    {
        World world = player.getWorld();
        if (world.isClient)
        {
            var random = world.getRandom();
            world.addParticle(ParticleTypes.FLAME, player.getX() + ((random.nextFloat() * 2f - 1f) * PARTICLE_RANGE), player.getY() + 1 + ((random.nextFloat() * 2f - 1f) * PARTICLE_RANGE),
                    player.getZ() + ((random.nextFloat() * 2f - 1f) * PARTICLE_RANGE), 0, 0.f, 0);
            world.addParticle(ParticleTypes.SMOKE, player.getX() + ((random.nextFloat() * 2f - 1f) * PARTICLE_RANGE), player.getY() + 1 + ((random.nextFloat() * 2f - 1f) * PARTICLE_RANGE),
                    player.getZ() + ((random.nextFloat() * 2f - 1f) * PARTICLE_RANGE), 0, 0.f, 0);
        }
    }

    public boolean isServer()
    {
        return player instanceof ServerPlayerEntity;
    }

    public boolean hasEnchantment()
    {
        return getEnchantmentLevel() > 0;
    }

    public int getEnchantmentLevel()
    {
        return EnchantmentHelper.getHighestEquippedLevel(player.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.SMOLDERING), player);
    }

    public void chargeBy(float amount)
    {
        this.charge += amount;

        if (charge > MAX_CHARGE)
        {
            detonate();
        }
        else
        {
            sync();
        }
    }

    public void detonate()
    {
        player.getWorld().createExplosion(player, player.getX(), player.getY(), player.getZ(), charge / 2, true, ExplosionSourceType.MOB);
        this.charge = 0;
        sync();
    }
    
    private void quench()
    {
        player.getWorld().playSound(player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.2f, 1f, false);
        this.charge = 0;
        sync();
    }

    public void sync()
    {
        ModComponentKeys.SMOLDERING.sync(player);
    }

    public float getChargePrecent()
    {
        return charge / MAX_CHARGE;
    }

    public boolean hasCharge()
    {
        return charge > 0;
    }

    public double expendCharge()
    {
        if (charge > 4.0)
        {
            charge -= 4.0;
            return 4.0;
        }
        else
        {
            double val = charge;
            charge = 0;
            return val;
        }
    }
}
