package com.skadoosh.wilderlands.items.crossbow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class RecoilCrossbowProjectileBehavior extends TriggeredCrossbowBehavior
{
    private final float recoilForce;
    
    public RecoilCrossbowProjectileBehavior(float recoilForce)
    {
        this.recoilForce = recoilForce;
    }

    @Override
    public void onTrigger(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow)
    {
        if (world.isClient)
        {
            world.addParticle(ParticleTypes.GUST_EMITTER_LARGE, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
        }
    }

    @Override
    public float getRecoil()
    {
        return recoilForce;
    }

    @Override
    public void applyRecoil(LivingEntity user, ItemStack weapon)
    {
        // dont call super to skip sneaking check
        user.addVelocity(user.getRotationVector().normalize().multiply(-1 * getRecoil()));
    }
}
