package com.skadoosh.wilderlands.screen.handler;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.feature_flags.FeatureFlags;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

@ClientOnly
public class ModScreenHandlers
{
    public static final ScreenHandlerType<AstralForgeCoreScreenHandler> ASTRAL_FORGE_CORE_SCREEN_HANDLER_TYPE = Registry.register(Registries.SCREEN_HANDLER_TYPE, Wilderlands.id("astral_forge_core_screen_handler_type"), new ScreenHandlerType<AstralForgeCoreScreenHandler>(AstralForgeCoreScreenHandler::new, FeatureFlags.DEFAULT_SET));

    public static void initialize()
    {

    }    
}
