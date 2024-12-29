package com.skadoosh.cadmium.animation;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public interface AnimationStep
{
    public int getDuration();
    public void tick(World world, int tick);

    public static AnimationStep particle(int duration, ParticleEffect particle, double x, double y, double z, double velX, double velY, double velZ, int count, double speed)
    {
        return new FixedAnimationStep(duration) {

            @Override
            public void tick(World world, int tick)
            {
                if (world instanceof ServerWorld sw)
                {
                    sw.spawnParticles(particle, x, y, z, count, velX, velY, velZ, speed);
                }
            }
        };
    }
    
}
