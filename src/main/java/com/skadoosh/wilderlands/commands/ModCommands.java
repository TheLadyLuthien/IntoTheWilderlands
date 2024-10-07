package com.skadoosh.wilderlands.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.ClickEvent.Action;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

public final class ModCommands
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BifrostCommand.register(dispatcher, registryAccess, environment);

            // TODO: remove
            dispatcher.register(literal("getitemdata").executes(contxtx -> {
                contxtx.getSource().sendFeedback(() -> {
                    try
                    {
                        String str = contxtx.getSource().getPlayerOrThrow().getMainHandStack().encode(registryAccess).toString();
                        return Text.literal(str).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(Action.COPY_TO_CLIPBOARD, str)));
                    }
                    catch (CommandSyntaxException e)
                    {
                        return Text.literal("error writing item");
                        // e.printStackTrace();
                    }
                }, false);
                return 0;
            }));
        });
    }
}
