package com.skadoosh.cadmium.animation;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.World;

public class ParticleAnimation
{
    private boolean playing = false;
    private int ticks = 0;

    private int lastStartedTick = 0;

    private final AnimationStep[] steps;

    @Nullable
    private AnimationStep currentStep = null;

    // public ParticleAnimation(AnimationStep[] steps)
    // {
    //     this.steps = steps;
    //     this.currentStep = getFirstStep();
    // }

    public ParticleAnimation(AnimationStep... steps)
    {
        this.steps = steps;
        this.currentStep = getFirstStep();
    }

    @Nullable
    public AnimationStep getFirstStep()
    {
        return steps.length > 0 ? steps[0] : null;
    }

    public void play()
    {
        if (!playing)
        {
            this.reset();
        }
        this.playing = true;
    }

    public boolean playing()
    {
        return this.playing;
    }

    public void stop()
    {
        this.playing = false;
    }

    public void setPlaying(boolean play)
    {
        if (play)
        {
            play();
        }
        else
        {
            stop();
        }
    }

    public void reset()
    {
        this.ticks = 0;
        this.lastStartedTick = 0;
        this.currentStep = getFirstStep();
    }

    public int getDuration()
    {
        return Arrays.stream(steps).mapToInt(s -> s.getDuration()).sum();
    }

    @Nullable
    private AnimationStep getNextStep()
    {
        int remaining = this.ticks;
        for (AnimationStep step : steps)
        {
            if (remaining <= 0)
            {
                return step;
            }

            remaining -= step.getDuration();
        }

        return null;
    }

    public void tick(World world)
    {
        // Checks if playing
        if (!playing || (currentStep == null))
        {
            return;
        }
        


        // Runs
        if (ticks - lastStartedTick >= currentStep.getDuration())
        {
            // current step is over, move to next
            currentStep = getNextStep();
            lastStartedTick = ticks;
        }
        
        currentStep.tick(world, ticks - lastStartedTick);


        // Cleanup and end
        ticks++;

        if (ticks >= this.getDuration())
        {
            playing = false;
        }
    }
}
