package com.skadoosh.wilderlands.items.crossbow;

import com.skadoosh.wilderlands.entities.BundleEntity;
import com.skadoosh.wilderlands.items.CrossbowProjectileBehavior;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BundleItemCrossbowBehavior extends CrossbowProjectileBehavior
{
    @Override
    public boolean allowLoading(LivingEntity user, ItemStack crossbow)
    {
        boolean bl = EnchantmentHelper.getLevel(user.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(Enchantments.MULTISHOT), crossbow) > 0;
        return !bl;
    }

    @Override
    public ProjectileEntity getProjectileEntity(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical)
    {
        BundleEntity projectile = new BundleEntity(world, entity);
        
        projectile.setItem(arrow);

        Vec3d pos = entity.getEyePos().subtract(0, 0.2, 0).add(entity.getRotationVector().multiply(0.2));
        projectile.setPos(pos.x, pos.y, pos.z);

        return projectile;
    }

    @Override
    public float getLaunchSpeed()
    {
        return 1.4f;
    }
}
