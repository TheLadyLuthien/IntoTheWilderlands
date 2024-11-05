package com.skadoosh.wilderlands.items.crossbow;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SonicBoomCrossbowProjectileBehavior extends TriggeredCrossbowBehavior
{
    public static final float MAX_DISTANCE = 20.0f;
    public static final float RADIUS = 2f;

    @Override
    public void onTrigger(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow)
    {
        Vec3d start = entity.getEyePos();
        Vec3d direction = entity.getRotationVector().normalize();

        if (world instanceof ServerWorld sw)
        {
            int i = MathHelper.floor(MAX_DISTANCE) + 7;
            for (int j = 1; j < i; j++)
            {
                Vec3d vec3d4 = start.add(direction.multiply((double)j));
                sw.spawnParticles(ParticleTypes.SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        entity.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0F, 1.0F);

        Set<LivingEntity> targets = getTargets(world, entity);

        for (LivingEntity livingEntity : targets)
        {
            if (livingEntity.damage(world.getDamageSources().sonicBoom(entity), 10.0F))
            {
                double d = 0.5 * (1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                double e = 2.5 * (1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                livingEntity.addVelocity(direction.getX() * e, direction.getY() * d, direction.getZ() * e);
            }
        }
    }

    private Set<LivingEntity> getTargets(World world, LivingEntity user)
    {
        Vec3d start = user.getEyePos();
        Vec3d direction = user.getRotationVector().normalize();

        // Store entities in a set to avoid duplicates
        Set<LivingEntity> entitiesInCylinder = new HashSet<>();

        // Iterate along the ray in steps to form a cylindrical path
        for (double i = 0; i <= MAX_DISTANCE; i += 1.0)
        {
            // Calculate the current point along the ray
            Vec3d point = start.add(direction.multiply(i));

            // Define a bounding box around the point with the cylinder's radius
            Box boundingBox = new Box(point.subtract(RADIUS, RADIUS, RADIUS), point.add(RADIUS, RADIUS, RADIUS));

            // Predicate to filter out entities within the radius cylinder around the ray
            Predicate<LivingEntity> entityFilter = entity -> entity.squaredDistanceTo(point) <= RADIUS * RADIUS && entity != user;

            // Find all entities within this bounding box and filter
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, boundingBox, entityFilter);

            // Add found entities to the set to avoid duplicates
            entitiesInCylinder.addAll(entities);
        }

        return entitiesInCylinder;
    }

    @Override
    public int getUseDamage()
    {
        return 8;
    }
}
