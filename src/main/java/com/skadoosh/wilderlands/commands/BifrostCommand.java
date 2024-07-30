package com.skadoosh.wilderlands.commands;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.regex.Matcher;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.skadoosh.wilderlands.blockentities.CarvedRunestoneBlockEntity;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.blocks.RunicKeystoneBlock;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.misc.BifrostHelper;
import com.skadoosh.wilderlands.misc.BifrostHelper.KeyType;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.NamedKeystoneData;
import com.skadoosh.wilderlands.persistance.NamedKeystoneData.KeystoneLocation;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.CommandBuildContext;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BifrostCommand
{
    private static final SimpleCommandExceptionType INVALID_SELECTION = new SimpleCommandExceptionType(Text.translatable("commands.bifrost.configure.invalid_selection"));
    private static final SimpleCommandExceptionType NO_TARGET_FOUND = new SimpleCommandExceptionType(Text.translatable("commands.bifrost.configure.no_target_found"));
    private static final SimpleCommandExceptionType POS_UNLOADED = new SimpleCommandExceptionType(Text.translatable("commands.bifrost.configure.unloaded_position"));
    private static final SimpleCommandExceptionType KEY_NOT_HELD = new SimpleCommandExceptionType(Text.translatable("commands.bifrost.configure.key_not_held"));
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandBuildContext registryAccess, RegistrationEnvironment environment)
    {
        dispatcher.register(
            literal("bifrost")
                .then(
                    literal("configure")
                    .requires(source -> source.hasPermission(2))

                    .then(
                        literal("runestone")
                        .then(
                            literal("select")
                            .executes(contextx -> selectRunestone(contextx, RunicKeystoneBlock.SEARCH_SIZE))
                            .then(
                                argument("radius", IntegerArgumentType.integer())
                                .executes(contextx -> selectRunestone(contextx, IntegerArgumentType.getInteger(contextx, "radius")))
                            )
                        )

                        .then(
                            literal("destination")
                            .then(
                                argument("pos", BlockPosArgumentType.blockPos())
                                .executes(contextx -> setStoneDestination(contextx, BlockPosArgumentType.getBlockPos(contextx, "pos"), contextx.getSource().getWorld()))
                                .then(
                                    argument("dimension", DimensionArgumentType.dimension())
                                    .executes(contextx -> setStoneDestination(contextx, BlockPosArgumentType.getBlockPos(contextx, "pos"), DimensionArgumentType.getDimensionArgument(contextx, "dimension")))
                                )
                            )
                            .then(
                                literal("selected_keystone")
                                .executes(contextx -> setStoneDestination(contextx, selectedKeystonePos, contextx.getSource().getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, selectedKeystoneWorld))))
                            )
                        )
                    )
                    .then(
                        literal("keystone")

                        .then(
                            literal("select")
                            .executes(contextx -> selectKeystone(contextx, RunicKeystoneBlock.SEARCH_SIZE))
                            .then(
                                argument("radius", IntegerArgumentType.integer())
                                .executes(contextx -> selectKeystone(contextx, IntegerArgumentType.getInteger(contextx, "radius")))
                            )
                            .then(
                                argument("name", StringArgumentType.string())
                                .executes(contextx -> selectKeystoneByName(contextx, StringArgumentType.getString(contextx, "name")))
                            )
                        )

                        .then(
                            literal("name")
                            .then(
                                argument("name", StringArgumentType.string())
                                .executes(contextx -> setKeystoneName(contextx, StringArgumentType.getString(contextx, "name")))
                            )
                        )
                    )
                    .then(
                        literal("list_names")
                        .executes(contextx -> listKeystoneNames(contextx))
                    )
                    .then(
                        literal("held_key")
                        .then(
                            literal("get")
                            .executes(contextx -> {
                                NbtComponent nbtComponent = BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()).get(ModComponents.BIFROST_KEY);
                                contextx.getSource().sendFeedback(() -> Text.literal(nbtComponent.toString()), false);
                                return 1;
                            })
                        )
                        .then(
                            literal("set")
                            .then(
                                literal("to_single_destination")
                                .then(
                                    argument("destination_name", StringArgumentType.string())
                                    .then(
                                        argument("uses_remaining", IntegerArgumentType.integer())
                                        .executes(contextx -> {
                                            BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                                .type(KeyType.TO_SINGLE_DESTINATION)
                                                .keystoneData(StringArgumentType.getString(contextx, "destination_name"))
                                                .usesRemaining(IntegerArgumentType.getInteger(contextx, "uses_remaining"));
                                            return 1;
                                        })
                                    )
                                    .then(
                                        literal("unlimited")
                                        .executes(contextx -> {
                                            BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                                .type(KeyType.TO_SINGLE_DESTINATION)
                                                .keystoneData(StringArgumentType.getString(contextx, "destination_name"))
                                                .usesRemaining(-1);
                                            return 1;
                                        })
                                    )
                                )
                            )
                            .then(
                                literal("to_from_single_destination")
                                .then(
                                    argument("keystone_name", StringArgumentType.string())
                                    .then(
                                        argument("uses_remaining", IntegerArgumentType.integer())
                                        .executes(contextx -> {
                                            BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                                .type(KeyType.TO_FROM_SINGLE_DESTINATION)
                                                .keystoneData(StringArgumentType.getString(contextx, "keystone_name"))
                                                .usesRemaining(IntegerArgumentType.getInteger(contextx, "uses_remaining"));
                                            return 1;
                                        })
                                    )
                                    .then(
                                        literal("unlimited")
                                        .executes(contextx -> {
                                            BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                                .type(KeyType.TO_FROM_SINGLE_DESTINATION)
                                                .keystoneData(StringArgumentType.getString(contextx, "keystone_name"))
                                                .usesRemaining(-1);
                                            return 1;
                                        })
                                    )
                                )
                            )
                            .then(
                                literal("to_single_dimension")
                                .then(
                                    argument("dimension", DimensionArgumentType.dimension())
                                    .then(
                                        argument("uses_remaining", IntegerArgumentType.integer())
                                        .executes(contextx -> {
                                            BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                                .type(KeyType.TO_SINGLE_DIMENSION)
                                                .dimension(DimensionArgumentType.getDimensionArgument(contextx, "dimension"))
                                                .usesRemaining(IntegerArgumentType.getInteger(contextx, "uses_remaining"));
                                            return 1;
                                        })
                                    )
                                    .then(
                                        literal("unlimited")
                                        .executes(contextx -> {
                                            BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                                .type(KeyType.TO_SINGLE_DIMENSION)
                                                .dimension(DimensionArgumentType.getDimensionArgument(contextx, "dimension"))
                                                .usesRemaining(-1);
                                            return 1;
                                        })
                                    )
                                )
                            )
                            .then(
                                literal("within_current_dimension")
                                .then(
                                    argument("uses_remaining", IntegerArgumentType.integer())
                                    .executes(contextx -> {
                                        BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                            .type(KeyType.WITHIN_CURRENT_DIMENSION)
                                            .usesRemaining(IntegerArgumentType.getInteger(contextx, "uses_remaining"));
                                        return 1;
                                    })
                                )
                                .then(
                                    literal("unlimited")
                                    .executes(contextx -> {
                                        BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                            .type(KeyType.WITHIN_CURRENT_DIMENSION)
                                            .usesRemaining(-1);
                                        return 1;
                                    })
                                )
                            )
                            .then(
                                literal("universal")
                                .then(
                                    argument("uses_remaining", IntegerArgumentType.integer())
                                    .executes(contextx -> {
                                        BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                            .type(KeyType.UNIVERSAL)
                                            .usesRemaining(IntegerArgumentType.getInteger(contextx, "uses_remaining"));
                                        return 1;
                                    })
                                )
                                .then(
                                    literal("unlimited")
                                    .executes(contextx -> {
                                        BifrostHelper.buildKey(BifrostHelper.getKeyStack(contextx.getSource().getPlayerOrThrow()))
                                            .type(KeyType.UNIVERSAL)
                                            .usesRemaining(-1);
                                        return 1;
                                    })
                                )
                            )
                        )
                    )
                )
                .then(
                    literal("identify")
                    .executes(contextx -> getKeystoneName(contextx))
                )
        );
    }

    private static BlockPos selectedRunestonePos = null;
    private static Identifier selectedRunestoneWorld = null;

    private static BlockPos selectedKeystonePos = null;
    private static Identifier selectedKeystoneWorld = null;

    private static int selectRunestone(CommandContext<ServerCommandSource> contextx, int radius) throws CommandSyntaxException
    {
        final Vec3d origin = contextx.getSource().getPosition();

        CarvedRunestoneBlockEntity closestRunestone = null;
        double prevDist = Double.MAX_VALUE;
        for (int x = -radius; x <= radius; x++)
        {
            for (int y = -radius; y <= radius; y++)
            {
                for (int z = -radius; z <= radius; z++)
                {
                    BlockPos pos = new BlockPos((int)origin.getX() + x, (int)origin.getY() + y, (int)origin.getZ() + z);
                    java.util.Optional<CarvedRunestoneBlockEntity> opt = contextx.getSource().getWorld().getBlockEntity(pos, ModBlockEntities.CARVED_RUNESTONE_BLOCK_ENTITY);
                    
                    if (opt.isPresent())
                    {
                        double dist = origin.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ());
                        if (dist < prevDist)
                        {
                            prevDist = dist;
                            closestRunestone = opt.get();
                        }
                    }
                }
            }
        }

        if (closestRunestone != null)
        {
            selectedRunestonePos = closestRunestone.getPos();
            selectedRunestoneWorld = closestRunestone.getWorld().getRegistryKey().getValue();
            return 1;
        }
        else
        {
            throw NO_TARGET_FOUND.create();
        }
    }

    private static int selectKeystoneByName(CommandContext<ServerCommandSource> contextx, String name) throws CommandSyntaxException
    {
        KeystoneLocation keystoneLocation = ModComponentKeys.NAMED_KEYSTONE_DATA.get(contextx.getSource().getServer().getOverworld()).get(BifrostHelper.desanitizeKeystoneName(name));
        if (keystoneLocation == null)
        {
            throw NO_TARGET_FOUND.create();
        }

        selectedKeystonePos = keystoneLocation.position;
        selectedKeystoneWorld = keystoneLocation.dimension;

        return 1;
    }

    private static int selectKeystone(CommandContext<ServerCommandSource> contextx, int radius) throws CommandSyntaxException
    {
        final Vec3d origin = contextx.getSource().getPosition();

        BlockPos closestPos = null;
        double prevDist = Double.MAX_VALUE;
        for (int x = -radius; x <= radius; x++)
        {
            for (int y = -radius; y <= radius; y++)
            {
                for (int z = -radius; z <= radius; z++)
                {
                    BlockPos pos = new BlockPos((int)origin.getX() + x, (int)origin.getY() + y, (int)origin.getZ() + z);
                    
                    BlockState blockState = contextx.getSource().getWorld().getBlockState(pos);
                    
                    if (blockState.isOf(ModBlocks.RUNIC_KEYSTONE))
                    {
                        double dist = origin.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ());
                        if (dist < prevDist)
                        {
                            prevDist = dist;
                            closestPos = pos;
                        }
                    }
                }
            }
        }

        if (closestPos != null)
        {
            selectedKeystonePos = closestPos;
            selectedKeystoneWorld = contextx.getSource().getWorld().getRegistryKey().getValue();
            return 1;
        }
        else
        {
            throw NO_TARGET_FOUND.create();
        }
    }

    private static int setStoneDestination(CommandContext<ServerCommandSource> contextx, BlockPos dest, ServerWorld destWorld) throws CommandSyntaxException
    {
        if (selectedRunestonePos == null || selectedRunestoneWorld == null)
        {
            throw INVALID_SELECTION.create();
        }

        RegistryKey<World> registryKey = RegistryKey.of(RegistryKeys.WORLD, selectedRunestoneWorld);
        final ServerWorld targetWorld = contextx.getSource().getServer().getWorld(registryKey);

        Chunk chunk = targetWorld.getChunk(selectedRunestonePos);
        if (targetWorld.isChunkLoaded(chunk.getPos().x, chunk.getPos().z))
        {
            BlockEntity blockEntity = targetWorld.getBlockEntity(selectedRunestonePos);
            if (blockEntity instanceof CarvedRunestoneBlockEntity)
            {
                ((CarvedRunestoneBlockEntity)blockEntity).setDestination(dest, destWorld);
                return 1;
            }
            else
            {
                throw INVALID_SELECTION.create();
            }
        }
        else
        {
            throw POS_UNLOADED.create();
        }
    }

    private static int setKeystoneName(CommandContext<ServerCommandSource> contextx, String name) throws CommandSyntaxException
    {
        if (selectedKeystonePos == null || selectedKeystoneWorld == null)
        {
            throw INVALID_SELECTION.create();
        }

        name = BifrostHelper.getTranslatedDimension(selectedKeystoneWorld).getString() + " - " + name;

        getNamedKeystoneData(contextx).map.put(new NamedKeystoneData.KeystoneLocation(selectedKeystoneWorld, selectedKeystonePos), name);
        return 1;
    }

    private static int getKeystoneName(CommandContext<ServerCommandSource> contextx) throws CommandSyntaxException
    {
        final Vec3d origin = contextx.getSource().getPosition();

        final int radius = RunicKeystoneBlock.SEARCH_SIZE;
        BlockPos closestPos = null;
        double prevDist = Double.MAX_VALUE;
        for (int x = -radius; x <= radius; x++)
        {
            for (int y = -radius; y <= radius; y++)
            {
                for (int z = -radius; z <= radius; z++)
                {
                    BlockPos pos = new BlockPos((int)origin.getX() + x, (int)origin.getY() + y, (int)origin.getZ() + z);
                    
                    BlockState blockState = contextx.getSource().getWorld().getBlockState(pos);
                    
                    if (blockState.isOf(ModBlocks.RUNIC_KEYSTONE))
                    {
                        double dist = origin.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ());
                        if (dist < prevDist)
                        {
                            prevDist = dist;
                            closestPos = pos;
                        }
                    }
                }
            }
        }

        if (closestPos != null)
        {
            String name = getNamedKeystoneData(contextx).get(contextx.getSource().getWorld().getRegistryKey().getValue(), closestPos);
            
            if (name == null)
            {
                name = "Unnamed Keystone";
            }
    
            final String n = name;
    
            contextx.getSource().sendFeedback(() -> Text.literal("You are currently at " + n), false);
    
            return 1;
        }
        else
        {
            throw NO_TARGET_FOUND.create();
        }
    }

    public static int listKeystoneNames(CommandContext<ServerCommandSource> contextx) throws CommandSyntaxException
    {
        for (String name : ModComponentKeys.NAMED_KEYSTONE_DATA.get(contextx.getSource().getServer().getOverworld()).map.values())
        {
            contextx.getSource().sendFeedback(() -> 
                Text.literal(name + " ")
                .append(
                    Text.literal("§a (copy) ")
                    .setStyle(
                        Style.EMPTY
                        .withClickEvent(
                            new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, BifrostHelper.sanitizeKeystoneName(name)))
                        .withHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("copy " + name + " to clipboard")))
                    )
                )
                .append(
                    Text.literal("§b (select) ")
                    .setStyle(
                        Style.EMPTY
                        .withClickEvent(
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bifrost configure keystone select \"" + BifrostHelper.sanitizeKeystoneName(name) + "\""))
                        .withHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("select this keystone")))
                    )
                ),
            false);
        }
        return 1;
    }

    private static NamedKeystoneData getNamedKeystoneData(CommandContext<ServerCommandSource> contextx)
    {
        return ModComponentKeys.NAMED_KEYSTONE_DATA.get(contextx.getSource().getServer().getOverworld());
    }
}
