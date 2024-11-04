package com.skadoosh.wilderlands.items.crossbow;

import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class ArrowCrossbowProjectileBehavior extends CrossbowProjectileBehavior
{
    @Override
    public ProjectileEntity getProjectileEntity(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical)
    {
        ArrowItem arrowItem2 = arrow.getItem() instanceof ArrowItem arrowItem ? arrowItem : (ArrowItem)Items.ARROW;
        PersistentProjectileEntity persistentProjectileEntity = arrowItem2.createArrowEntity(world, arrow, entity, weapon);
        if (isCritical)
        {
            persistentProjectileEntity.setCritical(true);
        }
        persistentProjectileEntity.setSound(SoundEvents.ITEM_CROSSBOW_HIT);

        return persistentProjectileEntity;
    }

    @Override
    public float getLaunchSpeed()
    {
        return 3.14f;
    }
}
