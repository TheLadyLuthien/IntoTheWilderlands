package com.skadoosh.cadmium.animation;

import com.skadoosh.cadmium.animation.values.ConstantValue;
import com.skadoosh.cadmium.animation.values.LinearValue;

import net.minecraft.util.math.MathHelper;

public interface AnimatedValue
{
    public double get(int tick, int duration);

    default public int asFlooredInt(int tick, int duration)
    {
        return MathHelper.floor(this.get(tick, duration));
    }

    public static AnimatedValue constant(double val)
    {
        return new ConstantValue(val);
    }

    public static AnimatedValue linear(double start, double end)
    {
        return new LinearValue(start, end);
    }
}
