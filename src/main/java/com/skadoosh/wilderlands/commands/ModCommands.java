package com.skadoosh.wilderlands.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class ModCommands
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            RunestoneCommand.register(dispatcher, registryAccess, environment);
        });
    }
}
