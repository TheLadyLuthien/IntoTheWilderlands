package com.skadoosh.wilderlands;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skadoosh.wilderlands.blockentities.ModBlockEntities;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.events.ModEvents;
import com.skadoosh.wilderlands.items.ModItems;

import net.minecraft.util.Identifier;

public class Wilderlands implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("Wilderlands");
    public static final String NAMESPACE = "wilderlands";

    @Override
    public void onInitialize(ModContainer mod)
    {
        ModBlocks.init();
        ModItems.init();
        ModComponents.init();
        ModBlockEntities.init();
        ModEvents.register();

        LOGGER.info("Hello Quilt world from {}! Stay fresh!", mod.metadata().name());
    }

    public static Identifier id(String name)
    {
        return Identifier.of(NAMESPACE, name);
    }
}
