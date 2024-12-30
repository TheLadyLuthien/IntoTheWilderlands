package com.skadoosh.cadmium.animation;

public abstract class FixedAnimationStep implements AnimationStep
{
    private final int duration;

    public FixedAnimationStep(int duration)
    {
        this.duration = duration;
    }

    @Override
    public int getDuration()
    {
        return duration;
    }

}
