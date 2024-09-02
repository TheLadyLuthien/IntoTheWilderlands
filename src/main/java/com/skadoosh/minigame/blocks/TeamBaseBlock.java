package com.skadoosh.minigame.blocks;

import java.util.Collection;

import com.skadoosh.minigame.Minigame;
import com.skadoosh.minigame.TeamRefrence;
import com.skadoosh.minigame.ZoneHelper;
import com.skadoosh.minigame.persistance.GameTeamData;
import com.skadoosh.minigame.persistance.NbtWorldPosValue;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.mixin.BannerBlockEntityAccessor;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Holder.Direct;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class TeamBaseBlock extends Block
{
    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
    {
        if (!world.isClient)
        {
            ServerWorld sw = (ServerWorld)world;
            sw.setChunkForced(world.getChunk(pos).getPos().x, world.getChunk(pos).getPos().z, false);
        }
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
    {
        if (!world.isClient)
        {
            ServerWorld sw = (ServerWorld)world;
            if (placer instanceof ServerPlayerEntity)
            {
                ServerPlayerEntity spe = (ServerPlayerEntity)placer;

                TeamRefrence team = TeamRefrence.of(spe);
                GameTeamData gtd = team.getData(world);
                if (gtd != null)
                {
                    gtd.getBaseLocation().setPos(pos, sw);
                    team.sendMessageToMembers(sw.getServer(), Text.literal("Base location set!"), true);
                }

                generateFlagAtBase(sw.getServer(), team);
            }

            sw.setChunkForced(world.getChunk(pos).getPos().x, world.getChunk(pos).getPos().z, true);
        }
    }

    public static void generateFlagAtBase(MinecraftServer server, TeamRefrence team)
    {
        NbtWorldPosValue baseLocation = team.getData(server.getScoreboard()).getBaseLocation();
        BlockPos pos = baseLocation.getPos().up();
        ServerWorld world = baseLocation.getWorld(server);

        world.setBlockState(pos, Blocks.WHITE_BANNER.getDefaultState());
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof BannerBlockEntity)
        {
            BannerBlockEntity bbe = (BannerBlockEntity)be;
            try
            {
                Text text = Text.literal(team.getName(server.getScoreboard()) + "'s Flag").setStyle(Style.EMPTY.withItalic(false).withBold(false));
                ((BannerBlockEntityAccessor)(Object)bbe).setCustomName(text);
                BannerBlockEntity.class.getField("teamId").set(bbe, team.getId());
            }
            catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
            {
                Minigame.LOGGER.error("Failed to set team ID on banner", e);
            }
        }
    }


    public TeamBaseBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity entity, BlockHitResult hitResult)
    {
        if (!world.isClient)
        {
            ServerWorld sw = (ServerWorld)world;

            TeamRefrence teamOfUser = TeamRefrence.of(entity);
            ItemStack stack = null;

            ItemStack mainHand = entity.getStackInHand(Hand.MAIN_HAND);
            ItemStack offHand = entity.getStackInHand(Hand.OFF_HAND);
            Hand hand = null;
            if (mainHand != null && !mainHand.isEmpty())
            {
                stack = mainHand;
                hand = Hand.MAIN_HAND;
            }
            else if (offHand != null && !offHand.isEmpty())
            {
                stack = offHand;
                hand = Hand.OFF_HAND;
            }

            if (stack == null || !stack.isOf(Items.WHITE_BANNER) || stack.get(ModComponents.FLAG_TEAM_ID) == null)
            {
                return ActionResult.PASS;
            }

            String bannerTeamId = stack.get(ModComponents.FLAG_TEAM_ID);
            TeamRefrence bannerTeam = TeamRefrence.of(bannerTeamId);

            Collection<String> teamNames = sw.getScoreboard().getTeamNames();
            for (String teamID : teamNames)
            {
                TeamRefrence teamOfBase = TeamRefrence.of(teamID);
                NbtWorldPosValue baseLocation = teamOfBase.getData(world).getBaseLocation();
                if (baseLocation.getWorld(world.getServer()).equals(world) && baseLocation.getPos().equals(pos))
                {
                    // this is a base
                    if (teamOfUser.getId().equals(teamOfBase.getId()) && !bannerTeamId.equals(teamID))
                    {  
                        // matching base & player teams, not their own flag
                        teamOfUser.getData(world).addCapture();

                        teamOfBase.sendMessageToMembers(sw.getServer(), entity.getDisplayName().copy().append(Text.literal(" captured " + bannerTeam.getName(sw) + "'s flag!")), false);
                        bannerTeam.sendMessageToMembers(sw.getServer(), Text.literal("Your flag was captured. It has now been returned to your base."), false);

                        NbtWorldPosValue bannerBaseLocation = bannerTeam.getData(world).getBaseLocation();
                        ServerWorld bannerBaseWorld = bannerBaseLocation.getWorld(sw.getServer());
                        
                        bannerBaseWorld.setBlockState(bannerBaseLocation.getPos().up(), Blocks.AIR.getDefaultState());
                        bannerBaseWorld.setBlockState(bannerBaseLocation.getPos().up().up(), Blocks.AIR.getDefaultState());
                        ((BlockItem)(stack.getItem())).place(new AutomaticItemPlacementContext(bannerBaseWorld, bannerBaseLocation.getPos().up(), Direction.DOWN, stack, Direction.DOWN));
                        
                        // TeamBaseBlock.generateFlagAtBase(sw.getServer(), bannerTeam);
                        entity.setStackInHand(hand, ItemStack.EMPTY);

                        return ActionResult.CONSUME;
                    }
                    else
                    {
                        return ActionResult.FAIL;
                    }
                }
            }
            return ActionResult.PASS;
        }
        return ActionResult.PASS;
    }

    // @Override
    // protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
    // {
    // ChunkPos chunkPos = world.getChunk(pos).getPos();
    // ModComponentKeys.GAME_WORLD_DATA.get(world.)
    // }
}
