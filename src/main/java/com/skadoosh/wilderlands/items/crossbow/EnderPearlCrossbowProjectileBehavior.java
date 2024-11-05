package com.skadoosh.wilderlands.items.crossbow;

import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnderPearlCrossbowProjectileBehavior extends CrossbowProjectileBehavior
{
    @Override
    public ProjectileEntity getProjectileEntity(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical)
    {
        EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, entity);
        enderPearlEntity.setItem(arrow);
        enderPearlEntity.setProperties(entity, entity.getPitch(), entity.getYaw(), 0.0F, 1.5F, 1.0F);
        return enderPearlEntity;
    }

    @Override
    public float getLaunchSpeed()
    {
        return 3.4f;
    }
}
