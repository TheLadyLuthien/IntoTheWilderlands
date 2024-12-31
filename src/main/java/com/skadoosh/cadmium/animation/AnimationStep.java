package com.skadoosh.cadmium.animation;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static AnimationStep particleRing(int duration, ParticleEffect particle, AnimatedValue x, AnimatedValue y, AnimatedValue z, AnimatedValue radius, AnimatedValue count, AnimatedValue offset, AnimatedValue speed)
    {
        return new FixedAnimationStep(duration)
        {
            @Override
            public void tick(World world, int tick)
            {
                final double angleIncrement = 2 * Math.PI / count.asFlooredInt(tick, duration);
                for (int i = 0; i < count.asFlooredInt(tick, duration); i++)
                {
                    double angle = (i * angleIncrement) + offset.get(tick, duration);
                    final double xOffset = ((radius.get(tick, duration) / 2) * Math.cos(angle));
                    final double zOffset = ((radius.get(tick, duration) / 2) * Math.sin(angle));

                    if (world instanceof ServerWorld sw)
                    {
                        sw.spawnParticles(particle, x.get(tick, duration) + xOffset, y.get(tick, duration), z.get(tick, duration) + zOffset, 1, 0, 0, 0,
                                speed.get(tick, duration));
                    }
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

    // public static AnimationStep subsequence(AnimationStep... steps)
    // {
    //     return new AnimationStep()
    //     {
    //         @Override
    //         public void tick(World world, int tick)
    //         {
    //             for (AnimationStep step : steps)
    //             {
    //                 if (tick < step.getDuration())
    //                 {
    //                     step.tick(world, tick);
    //                 }
    //             }
    //         }

    //         @Override
    //         public int getDuration()
    //         {
    //             return Arrays.stream(steps).mapToInt(s -> s.getDuration()).sum();
    //         }
    //     };
    // }

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

    public static AnimationStep branch(BooleanSupplier predicate, AnimationStep a, AnimationStep b)
    {
        return new AnimationStep()
        {
            @Override
            public void tick(World world, int tick)
            {
                if (predicate.getAsBoolean())
                {
                    a.tick(world, tick);
                }
                else
                {
                    b.tick(world, tick);
                }
            }

            @Override
            public int getDuration()
            {
                if (predicate.getAsBoolean())
                {
                    return a.getDuration();
                }
                else
                {
                    return b.getDuration();
                }
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

    public static AnimationStep next(ParticleAnimation next)
    {
        return new FixedAnimationStep(1)
        {
            @Override
            public void tick(World world, int tick)
            {
                next.reset();
                next.play();
            }
        };
    }
}
