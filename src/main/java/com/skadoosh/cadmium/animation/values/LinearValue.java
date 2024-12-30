package com.skadoosh.cadmium.animation.values;

import com.skadoosh.cadmium.animation.AnimatedValue;

import net.minecraft.util.math.MathHelper;

public class LinearValue implements AnimatedValue
{
    private final double start;
    private final double end;

    public LinearValue(double start, double end)
    {
        this.start = start;
        this.end = end;
    }

    @Override
    public double get(int tick, int duration)
    {
        final double percent = ((double)tick / ((double)duration - 1.0));
        return MathHelper.lerp(percent, start, end);
    }
}
