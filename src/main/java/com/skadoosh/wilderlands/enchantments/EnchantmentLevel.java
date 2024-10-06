package com.skadoosh.wilderlands.enchantments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnchantmentLevel
{
    public static enum Level
    {
        A,B,C,STAR
    }

    public Level value();
}
