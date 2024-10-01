package com.skadoosh.wilderlands.blockentities;

import com.skadoosh.wilderlands.screen.handler.AstralForgeCoreScreenHandler;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class AstralForgeCoreBlockEntity extends BlockEntity implements NamedScreenHandlerFactory
{
    public AstralForgeCoreBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.ASTRAL_FORGE_CORE_BLCOK_ENTITY, pos, state);
    }

    @Override
    public ScreenHandler createMenu(int arg0, PlayerInventory arg1, PlayerEntity arg2)
    {
        return new AstralForgeCoreScreenHandler(arg0, arg1);
    }

    @Override
    public Text getDisplayName()
    {
        return Text.literal("tesan sakdjbn");
    }
}
