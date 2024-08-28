package com.skadoosh.minigame.blocks;

import com.skadoosh.minigame.Minigame;
import com.skadoosh.minigame.blockentities.GravestoneBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerContentsComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GravestoneBlock extends Block implements BlockEntityProvider
{
    public GravestoneBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
    {
        return new GravestoneBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity entity, BlockHitResult hitResult)
    {
        GravestoneBlockEntity gbe = world.getBlockEntity(pos, Minigame.GRAVESTONE_BLOCK_ENTITY_TYPE).orElse(null);
        if (gbe != null)
        {
            boolean canOpen = false;
            if (gbe.getTeam() != GravestoneBlockEntity.NO_TEAM)
            {
                // check the team
                canOpen = world.getScoreboard().getPlayerTeam(entity.getProfileName()).getName().equals(gbe.getTeam());
            }
            else
            {
                canOpen = true;
            }

            // for other mods: just set to alaways run
            if (canOpen)
            {
                ItemStack tokenStack = new ItemStack(Minigame.GRAVE_TOKEN, 1);
                tokenStack.set(DataComponentTypes.CONTAINER, ContainerContentsComponent.fromStacks(gbe.getItems()));
                tokenStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(gbe.getPlayerName() + "'s Grave Token").setStyle(Style.EMPTY.withItalic(false).withBold(false)));

                if (!world.isClient)
                {
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, tokenStack);
                    itemEntity.setPickupDelay(40);

                    world.spawnEntity(itemEntity);

                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }

                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    protected boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos)
    {
        return false;
    }
}
