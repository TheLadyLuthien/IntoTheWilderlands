package com.skadoosh.cadmium.animation.values;

import com.skadoosh.cadmium.animation.AnimatedValue;

public class ConstantValue implements AnimatedValue
{
    private final double value;

    public ConstantValue(double value)
    {
        this.value = value;
    }

    @Override
    public double get(int tick, int duration)
    {
        return value;
    }
}
