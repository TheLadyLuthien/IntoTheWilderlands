package com.skadoosh.minigame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

public class Minigame
{
    public static final Logger LOGGER = LoggerFactory.getLogger("Wilderlands_Fall_Game");
    public static final String NAMESPACE = "fall_games";

    public static void initialize()
    {

    }

    public static Identifier id(String name)
    {
        return Identifier.of(NAMESPACE, name);
    }
}
