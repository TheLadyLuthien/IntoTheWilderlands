package com.skadoosh.wilderlands.items.crossbow;

import com.skadoosh.wilderlands.entities.TorchEntity;
import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TorchCrossbowProjectileBehavior extends CrossbowProjectileBehavior
{
    @Override
    public float getLaunchSpeed()
    {
        return 2.1f;
    }

    @Override
    public ProjectileEntity getProjectileEntity(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical)
    {
        return new TorchEntity(world, entity, arrow, weapon);
    }
}
