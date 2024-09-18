package com.skadoosh.wilderlands.datagen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoBlockLoot
{
    boolean requireSilkTouch() default false;
    ToolLevel requiresTool() default ToolLevel.NONE;
    MiningTool prefersTool() default MiningTool.NONE;
}
