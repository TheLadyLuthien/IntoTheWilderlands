package com.skadoosh.minigame.blocks;

import com.skadoosh.minigame.TeamRefrence;
import com.skadoosh.minigame.ZoneHelper;
import com.skadoosh.minigame.persistance.GameTeamData;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class TeamBaseBlock extends Block
{
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack)
    {
        if (!world.isClient)
        {
            if (placer instanceof ServerPlayerEntity)
            {
                ServerWorld sw = (ServerWorld)world;
                ServerPlayerEntity spe = (ServerPlayerEntity)placer;

                TeamRefrence team = TeamRefrence.of(spe);
                GameTeamData gtd = team.getData(world);
                if (gtd != null)
                {
                    gtd.getBaseLocation().setPos(pos, sw);
                    spe.sendMessage(Text.literal("Base location set!"), true);
                }
            }
        }
    }

    public TeamBaseBlock(Settings settings)
    {
        super(settings);
    }

    // @Override
    // protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
    // {
    //     ChunkPos chunkPos = world.getChunk(pos).getPos();
    //     ModComponentKeys.GAME_WORLD_DATA.get(world.)
    // }
}
