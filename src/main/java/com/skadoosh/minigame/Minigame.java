package com.skadoosh.minigame;

import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skadoosh.minigame.blockentities.GravestoneBlockEntity;
import com.skadoosh.minigame.blocks.GravestoneBlock;
import com.skadoosh.minigame.blocks.TeamBaseBlock;
import com.skadoosh.minigame.items.GraveTokenItem;
import com.skadoosh.minigame.voicechat.PluginEntrypoint;
import com.skadoosh.wilderlands.blocks.ModBlocks;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.Group.Type;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.DeltaTracker;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class Minigame
{
    public static final Logger LOGGER = LoggerFactory.getLogger("Wilderlands_Fall_Game");
    public static final String NAMESPACE = "fall_games";

    public static final Block GRAVESTONE_BLOCK = Registry.register(Registries.BLOCK, id("gravestone"), new GravestoneBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK)));
    public static final BlockEntityType<GravestoneBlockEntity> GRAVESTONE_BLOCK_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("gravestone"), BlockEntityType.Builder.create(GravestoneBlockEntity::new, ModBlocks.DEPLOYER).build());

    public static final Item GRAVE_TOKEN = Registry.register(Registries.ITEM, id("grave_token"), new GraveTokenItem(new Item.Settings().fireproof().maxCount(1)));
    public static final Item EVERSTAR = Registry.register(Registries.ITEM, id("everstar"), new Item(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)));

    public static final Block TEAM_BASE = Registry.register(Registries.BLOCK, id("team_base"), new TeamBaseBlock(AbstractBlock.Settings.copy(Blocks.BEDROCK)));
    public static final Item TEAM_BASE_ITEM = Registry.register(Registries.ITEM, id("team_base"), new BlockItem(TEAM_BASE, new Item.Settings().rarity(Rarity.EPIC).fireproof().maxCount(1)));

    public static void initialize()
    {
        ServerLifecycleEvents.SERVER_STARTED.register(new ServerStarted()
        {
            @Override
            public void onServerStarted(MinecraftServer server)
            {
                ServerScoreboard sb = server.getScoreboard();
                Collection<String> teamNames = sb.getTeamNames();
                for (String id : teamNames)
                {
                    TeamRefrence tr = TeamRefrence.of(id);
                    final String password = UUID.randomUUID().toString();

                    final var group = PluginEntrypoint.SERVER_API.groupBuilder().setName(tr.getName(sb)).setPassword(password).setPersistent(true).setType(Type.OPEN).build();
                    tr.getData(sb).setVoiceGroup(group);

                    Minigame.LOGGER.info("Password for " + id + "'s group is: " + password);
                }
            }
        });
    }

    public static Identifier id(String name)
    {
        return Identifier.of(NAMESPACE, name);
    }
}
