package com.skadoosh.wilderlands;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skadoosh.cadmium.Cadmium;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.commands.ModCommands;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.events.ModEvents;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.items.itemGroup.ModItemGroups;
import com.skadoosh.wilderlands.misc.BeheadingReloadListener;
import com.skadoosh.wilderlands.misc.ModParticles;
import com.skadoosh.wilderlands.screen.handler.ModScreenHandlers;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class Wilderlands implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("Wilderlands");
    public static final String NAMESPACE = "wilderlands";

    @Override
    public void onInitialize(ModContainer mod)
    {
        Cadmium.initialize();
        
        ModParticles.init();
        ModEnchantments.init();
        ModScreenHandlers.init();
        ModBlocks.init();
        ModItems.init();
        ModComponents.init();
        ModBlockEntities.init();
        
        ModItemGroups.register();
        ModEvents.register();
        ModCommands.register();


		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(BeheadingReloadListener.ID, BeheadingReloadListener::new);

        // LOGGER.info("Hello Quilt world from {}! Stay fresh!", mod.metadata().name());
    }

    public static Identifier id(String name)
    {
        return Identifier.of(NAMESPACE, name);
    }
}
