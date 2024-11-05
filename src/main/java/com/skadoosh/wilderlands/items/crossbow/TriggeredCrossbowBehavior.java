package com.skadoosh.wilderlands.items.crossbow;

import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class TriggeredCrossbowBehavior extends CrossbowProjectileBehavior
{
    public abstract void onTrigger(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow);

    @Override
    public ProjectileEntity getProjectileEntity(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical)
    {
        return null;
    }
}
