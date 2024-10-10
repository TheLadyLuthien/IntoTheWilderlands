package com.skadoosh.wilderlands.persistance;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import org.ladysnake.cca.api.v3.entity.C2SSelfMessagingComponent;

import com.skadoosh.wilderlands.enchantments.ModEnchantments;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
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

public class LiftComponent implements C2SSelfMessagingComponent, AutoSyncedComponent, ServerTickingComponent
{
    public static final int COOLDOWN = 30;
    public static final float JUMP_STRENGTH = 0.52f;

    private final PlayerEntity player;

    private int cooldown = 0;
    private int jumpsRemaining = 0;

    public LiftComponent(PlayerEntity player)
    {
        this.player = player;
    }

    public boolean canUse()
    {
        return (cooldown <= 0) && (jumpsRemaining > 0 || (player instanceof ClientPlayerEntity)) && !(player.isOnGround() || player.isClimbing()) && (player.getVelocity().y < 0) && (!player.getAbilities().flying);
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
    public void serverTick()
    {
        if (cooldown > 0)
        {
            cooldown--;
        }

        if (player.isOnGround() || player.isClimbing())
        {
            jumpsRemaining = EnchantmentHelper.getHighestEquippedLevel(player.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.LIFT), player);
        }
    }

    public void trigger()
    {
        sendC2SMessage(buf -> {});
    }

    @Override
    public void handleC2SMessage(RegistryByteBuf buf)
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

            ((ServerWorld)(player.getWorld())).spawnParticles(ParticleTypes.GUST, player.getX(), player.getY(), player.getZ(), jumpsRemaining, 1.0f, 1.0f, 1.0f, 1.25f);


            player.velocityDirty = true;
            jumpsRemaining--;
            cooldown = COOLDOWN;
        }
    }
}
