package com.skadoosh.wilderlands.items.crossbow;

import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.skadoosh.wilderlands.damage.ModDamageTypes;
import com.skadoosh.wilderlands.networking.payload.BrimstoneHitPayload;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BrimstoneCrossbowProjectileBehavior extends TriggeredCrossbowBehavior
{
    public static final float MAX_DISTANCE = 200.0f;
    public static final float BOX_RADIUS = 4.0f;
    public static final float POINT_RADIUS = 1.5f;
    public static final float STEP = 1.0f;

    public static final int DAMAGE = 26;

    public static final int PARTICLE_COUNT = 800;

    @Override
    public void onTrigger(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow)
    {
        // Vec3d start = entity.getEyePos();
        // Vec3d direction = entity.getRotationVector().normalize();

        entity.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), 3.0F, 1.0F);

        LivingEntity target = getTarget(world, entity);

        if (target != null)
        {
            target.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 3.0F, 1.3F);

            final DamageSource damageSource = new DamageSource(entity.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.BRIMSTONE));
            target.damage(damageSource, DAMAGE);
            entity.damage(damageSource, DAMAGE);

            if (world instanceof ServerWorld sw)
            {
                // double dist = start.subtract(target.getEyePos()).length();
                // for (double j = 1; j < dist; j += 5)
                // {
                // Vec3d vec3d4 = start.add(direction.multiply((double)j));
                // // ModParticles.BRIMSTONE.spawn(sw, vec3d4.x, vec3d4.y, vec3d4.z, )
                // sw.spawnParticles(ParticleTypes.EXPLOSION, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
                // }

                Vec3d vec3d4 = target.getEyePos();

                for (ServerPlayerEntity player : PlayerLookup.tracking(target))
                {
                    ServerPlayNetworking.send(player, new BrimstoneHitPayload(target.getPos().toVector3f()));
                }

                sw.spawnParticles(ParticleTypes.FLASH, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 1.0);
                sw.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, vec3d4.x, vec3d4.y, vec3d4.z, 2, 0.0, 0.0, 0.0, 1.0);
            }
            // if ()
            // {
            // double d = 0.5 * (1.0 -
            // livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            // double e = 2.5 * (1.0 -
            // livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            // livingEntity.addVelocity(direction.getX() * e, direction.getY() * d, direction.getZ() * e);
            // }
        }
    }

    @Nullable
    private LivingEntity getTarget(World world, LivingEntity user)
    {
        Vec3d start = user.getEyePos();
        Vec3d direction = user.getRotationVector().normalize();

        for (double i = 0; i <= MAX_DISTANCE; i += STEP)
        {
            // Calculate the current point along the ray
            Vec3d point = start.add(direction.multiply(i));

            // Define a bounding box around the point with the cylinder's radius
            Box boundingBox = new Box(point.subtract(BOX_RADIUS, BOX_RADIUS, BOX_RADIUS), point.add(BOX_RADIUS, BOX_RADIUS, BOX_RADIUS));

            // Predicate to filter out entities within the radius cylinder around the ray
            Predicate<LivingEntity> entityFilter = entity -> entity.getEyePos().squaredDistanceTo(point) <= POINT_RADIUS * POINT_RADIUS && entity != user;

            // Find all entities within this bounding box and filter
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, boundingBox, entityFilter);

            if (!entities.isEmpty())
            {
                return entities.getFirst();
            }
        }

        return null;
    }

    @Override
    public int getUseDamage()
    {
        return 10;
    }

    @Override
    public float getRecoil()
    {
        return 0.6f;
    }

    @Environment(EnvType.CLIENT)
    public static void spawnClientParticles(MinecraftClient client, Vector3f pos)
    {
        Vec3d vec3d = new Vec3d(pos);
        BlockStateParticleEffect blockStateParticleEffect = new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, Blocks.REDSTONE_BLOCK.getDefaultState());

        int i = PARTICLE_COUNT;

        for (int j = 0; (float)j < (float)i / 3.0F; j++)
        {
            double d = vec3d.x + client.getCameraEntity().getRandom().nextGaussian() / 2.0;
            double e = vec3d.y;
            double f = vec3d.z + client.getCameraEntity().getRandom().nextGaussian() / 2.0;
            double g = client.getCameraEntity().getRandom().nextGaussian() * 0.2F;
            double h = client.getCameraEntity().getRandom().nextGaussian() * 0.2F;
            double k = client.getCameraEntity().getRandom().nextGaussian() * 0.2F;
            client.getCameraEntity().getWorld().addParticle(blockStateParticleEffect, d, e, f, g, h, k);
        }

        for (int j = 0; (float)j < (float)i / 1.5F; j++)
        {
            double d = vec3d.x + 3.5 * Math.cos((double)j) + client.getCameraEntity().getRandom().nextGaussian() / 2.0;
            double e = vec3d.y;
            double f = vec3d.z + 3.5 * Math.sin((double)j) + client.getCameraEntity().getRandom().nextGaussian() / 2.0;
            double g = client.getCameraEntity().getRandom().nextGaussian() * 0.05F;
            double h = client.getCameraEntity().getRandom().nextGaussian() * 0.05F;
            double k = client.getCameraEntity().getRandom().nextGaussian() * 0.05F;
            client.getCameraEntity().getWorld().addParticle(blockStateParticleEffect, d, e, f, g, h, k);
        }
    }
}
