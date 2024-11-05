package com.skadoosh.wilderlands.items.crossbow;

import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PotionCrossbowProjectileBehavior extends CrossbowProjectileBehavior
{
    @Override
    public ProjectileEntity getProjectileEntity(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical)
    {
        PotionEntity potionEntity = new PotionEntity(world, entity);
        potionEntity.setItem(arrow);
        potionEntity.setProperties(entity, entity.getPitch(), entity.getYaw(), -20.0F, 0.5F, 1.0F);
        return potionEntity;
    }

    @Override
    public float getLaunchSpeed()
    {
        return 3f;
    }
}
