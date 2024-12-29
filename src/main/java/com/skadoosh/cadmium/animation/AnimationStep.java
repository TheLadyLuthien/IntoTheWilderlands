package com.skadoosh.cadmium.animation;

import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public interface AnimationStep
{
    public int getDuration();

    public void tick(World world, int tick);

    public static AnimationStep particle(int duration, ParticleEffect particle, AnimatedValue x, AnimatedValue y, AnimatedValue z, AnimatedValue velX, AnimatedValue velY, AnimatedValue velZ, AnimatedValue count, AnimatedValue speed)
    {
        return new FixedAnimationStep(duration)
        {

            @Override
            public void tick(World world, int tick)
            {
                if (world instanceof ServerWorld sw)
                {
                    sw.spawnParticles(particle, x.get(tick, duration), y.get(tick, duration), z.get(tick, duration), count.asFlooredInt(tick, duration), velX.get(tick, duration), velY.get(tick, duration), velZ.get(tick, duration),
                            speed.get(tick, duration));
                }
            }
        };
    }

    public static AnimationStep multi(int duration, AnimationStep... steps)
    {
        return new FixedAnimationStep(duration)
        {
            @Override
            public void tick(World world, int tick)
            {
                for (AnimationStep step : steps)
                {
                    if (tick < step.getDuration())
                    {
                        step.tick(world, tick);
                    }
                }
            }
        };
    }

    public static AnimationStep delay(int duration)
    {
        return new FixedAnimationStep(duration)
        {
            @Override
            public void tick(World world, int tick)
            {
            }
        };
    }

    public static AnimationStep event(Consumer<World> event)
    {
        return new FixedAnimationStep(1)
        {
            @Override
            public void tick(World world, int tick)
            {
                event.accept(world);
            }
        };
    }
}
