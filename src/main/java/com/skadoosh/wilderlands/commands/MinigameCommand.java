package com.skadoosh.wilderlands.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.skadoosh.minigame.DeathHelper;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.command.CommandBuildContext;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.AbstractTeam.VisibilityRule;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.server.command.ServerCommandSource;

public class MinigameCommand
{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext registryAccess, RegistrationEnvironment environment)
    {
        dispatcher.register(
            literal("mg").then(
                literal("setup")
                .then(
                    literal("reset")
                    .executes(contxtx -> {
                        Collection<String> teamNames = contxtx.getSource().getServer().getScoreboard().getTeamNames();
                        for (String name : teamNames)
                        {
                            contxtx.getSource().getServer().getScoreboard().removeTeam(contxtx.getSource().getServer().getScoreboard().getTeam(name));
                        }
                        
                        return 0;
                    })
                )
                .then(
                    literal("teams")
                    .then(
                        argument("count", IntegerArgumentType.integer(2))
                        .executes(contxtx -> {
                            final int count = IntegerArgumentType.getInteger(contxtx, "count");
                            for (int i = 0; i < count; i++)
                            {
                                final String teamId = "team_" + (i + 1);
                                final String teamName = "Team " + (i + 1);
                                final Team team = contxtx.getSource().getServer().getScoreboard().addTeam(teamId);
                                team.setDisplayName(Text.literal(teamName));
                                team.setFriendlyFireAllowed(false);
                                team.setNameTagVisibilityRule(VisibilityRule.HIDE_FOR_OTHER_TEAMS);
                                team.setShowFriendlyInvisibles(true);
                                team.setDeathMessageVisibilityRule(VisibilityRule.HIDE_FOR_OTHER_TEAMS);
                                team.setColor(Formatting.RESET);
                            }
                            return 0;
                        })
                    )
                )
            )
        );

        dispatcher.register(literal("lives").executes(contxtx -> {
            int lives = ModComponentKeys.GAME_PLAYER_DATA.get((contxtx.getSource().getPlayerOrThrow())).getLives();
            contxtx.getSource().sendFeedback(() -> Text.literal("You have " + lives + ((lives == 1) ? " life" : " lives") + " remaining").formatted(DeathHelper.getNameColor(lives)), false);

            return 0;
        }));
    }
}
