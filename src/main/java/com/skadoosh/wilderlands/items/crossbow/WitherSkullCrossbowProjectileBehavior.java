package com.skadoosh.wilderlands.items.crossbow;

import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WitherSkullCrossbowProjectileBehavior extends CrossbowProjectileBehavior
{
    @Override
    public float getLaunchSpeed()
    {
        return 1f;
    }

    @Override
    public ProjectileEntity getProjectileEntity(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical)
    {
        var fbe = new WitherSkullEntity(world, entity, Vec3d.ZERO);
        if (entity.getRandom().nextInt(3) == 1)
        {
            fbe.setCharged(true);
        }

        Vec3d pos = entity.getEyePos().subtract(0, 0.2, 0).add(entity.getRotationVector().multiply(0.2));
        fbe.setPos(pos.x, pos.y, pos.z);

        return fbe;
    }

    @Override
    public int getUseDamage()
    {
        return 4;
    }
}
