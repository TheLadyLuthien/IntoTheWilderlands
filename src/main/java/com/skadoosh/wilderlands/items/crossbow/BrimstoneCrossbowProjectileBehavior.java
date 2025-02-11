package com.skadoosh.wilderlands.items.crossbow;

import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.skadoosh.wilderlands.damage.ModDamageTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BrimstoneCrossbowProjectileBehavior extends TriggeredCrossbowBehavior
{
    public static final float MAX_DISTANCE = 200.0f;
    public static final float BOX_RADIUS = 4.0f;
    public static final float POINT_RADIUS = 0.5f;
    public static final float STEP = 3.0f;

    public static final int DAMAGE = 26;

    @Override
    public void onTrigger(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow)
    {
        Vec3d start = entity.getEyePos();
        Vec3d direction = entity.getRotationVector().normalize();

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
                //     Vec3d vec3d4 = start.add(direction.multiply((double)j));
                //     // ModParticles.BRIMSTONE.spawn(sw, vec3d4.x, vec3d4.y, vec3d4.z, )
                //     sw.spawnParticles(ParticleTypes.EXPLOSION, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
                // }

                Vec3d vec3d4 = target.getEyePos();

                sw.spawnParticles(ParticleTypes.FLASH, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 1.0);
                sw.spawnParticles(new BlockStateParticleEffect(ParticleTypes.DUST_PILLAR, Blocks.REDSTONE_BLOCK.getDefaultState()), vec3d4.x, vec3d4.y, vec3d4.z, 4, 0.0, 0.0, 0.0, 1.0);
                sw.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, vec3d4.x, vec3d4.y, vec3d4.z, 2, 0.0, 0.0, 0.0, 1.0);
            }
            // if ()
            // {
                //     double d = 0.5 * (1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            //     double e = 2.5 * (1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            //     livingEntity.addVelocity(direction.getX() * e, direction.getY() * d, direction.getZ() * e);
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
}
