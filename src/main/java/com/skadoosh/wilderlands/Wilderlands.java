package com.skadoosh.wilderlands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skadoosh.cadmium.Cadmium;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.commands.ModCommands;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.damage.ModDamageTypes;
import com.skadoosh.wilderlands.dimension.ModDimensions;
import com.skadoosh.wilderlands.dimension.biome.ModBiomes;
import com.skadoosh.wilderlands.effects.ModEffects;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.entities.ModEntities;
import com.skadoosh.wilderlands.events.ModEvents;
import com.skadoosh.wilderlands.items.CrossbowProjectileBehaviors;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.items.itemGroup.ModItemGroups;
import com.skadoosh.wilderlands.misc.BeheadingReloadListener;
import com.skadoosh.wilderlands.misc.ModChunkTickets;
import com.skadoosh.wilderlands.misc.ModLootConditionTypes;
import com.skadoosh.wilderlands.misc.ModParticles;
import com.skadoosh.wilderlands.networking.ModPackets;
import com.skadoosh.wilderlands.screen.handler.ModScreenHandlers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class Wilderlands implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("Wilderlands");
    public static final String NAMESPACE = "wilderlands";

    @Override
    public void onInitialize()
    {
        Cadmium.initialize();
        
        ModPackets.init();
        ModParticles.init();
        ModChunkTickets.init();
        ModDamageTypes.init();
        ModEffects.init();
        ModEnchantments.init();
        ModScreenHandlers.init();
        ModLootConditionTypes.init();
        ModBlocks.init();
        ModItems.init();
        ModComponents.init();
        ModBiomes.init();
        ModDimensions.init();
        ModBlockEntities.init();
        ModEntities.init();
        
        ModItemGroups.register();
        ModEvents.register();
        ModCommands.register();

        CrossbowProjectileBehaviors.init();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(BeheadingReloadListener.ID, BeheadingReloadListener::new);
    }

    public static Identifier id(String name)
    {
        return Identifier.of(NAMESPACE, name);
    }
}
