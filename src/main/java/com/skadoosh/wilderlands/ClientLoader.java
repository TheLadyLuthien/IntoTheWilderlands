package com.skadoosh.wilderlands;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import com.skadoosh.cadmium.Cadmium;

public class ClientLoader implements ClientModInitializer
{
    @Override
    public void onInitializeClient(ModContainer mod)
    {
        Wilderlands.LOGGER.info("Wilderlands Client Loaded");
        // Cadmium.initializeClient();
    }
}
