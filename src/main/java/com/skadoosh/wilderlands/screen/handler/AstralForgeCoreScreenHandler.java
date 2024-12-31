package com.skadoosh.wilderlands.screen.handler;

import java.util.Optional;

import com.skadoosh.wilderlands.blockentities.AstralForgeCoreBlockEntity;
import com.skadoosh.wilderlands.blockentities.ModBlockEntities;
import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.datagen.Datagen;
import com.skadoosh.wilderlands.misc.BifrostHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class AstralForgeCoreScreenHandler extends ScreenHandler
{
    public AstralForgeCoreScreenHandler(int syncId, Inventory inventory)
    {
        this(syncId, inventory, new ArrayPropertyDelegate(3), ScreenHandlerContext.EMPTY);
    }

    private final ScreenHandlerContext context;
    private final Inventory inventory = new SimpleInventory(4);
    private final Slot baseSlot;
    private final Slot keySlot;
    private final Slot reagent1Slot;
    private final Slot reagent2Slot;

    public AstralForgeCoreScreenHandler(int syncId, Inventory playerInventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context)
    {
        super(ModScreenHandlers.ASTRAL_FORGE_CORE_SCREEN_HANDLER_TYPE, syncId);
        ScreenHandler.checkDataCount(propertyDelegate, 3);
        this.addProperties(propertyDelegate);

        this.context = context;

        baseSlot = new Slot(this.inventory, 0, 27, 35)
        {
            @Override
            public boolean canInsert(ItemStack stack)
            {
                return (!(stack.getItem() instanceof BlockItem)) && (!stack.isIn(Datagen.ItemTagGenerator.ASTRAL_FORGE_REJECTED));
            }

            @Override
            public int getMaxItemCount()
            {
                return 1;
            }
        };

        keySlot = new Slot(this.inventory, 1, 80, 35)
        {
            @Override
            public boolean canInsert(ItemStack stack)
            {
                var key = stack.get(ModComponents.BIFROST_KEY);

                return key != null;
            }

            @Override
            public int getMaxItemCount()
            {
                return 1;
            }
        };

        reagent1Slot = new Slot(this.inventory, 2, 125, 13)
        {
            @Override
            public int getMaxItemCount()
            {
                return 1;
            }
        };
        reagent2Slot = new Slot(this.inventory, 3, 141, 13)
        {
            @Override
            public int getMaxItemCount()
            {
                return 1;
            }
        };

        this.addSlot(baseSlot);
        this.addSlot(keySlot);
        this.addSlot(reagent1Slot);
        this.addSlot(reagent2Slot);

        this.addPlayerInventory(playerInventory, 8, 84);
    }

    private void addPlayerInventory(Inventory playerInventory, int x, int y)
    {
        int k;
        for (k = 0; k < 3; ++k) // row
        {
            for (int l = 0; l < 9; ++l) // collunm
            {
                this.addSlot(new Slot(playerInventory, l + k * 9 + 9, // Index, +9 for hotbar
                        x + (l * 18), y + (k * 18)));
            }
        }

        for (k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInventory, k, x + (k * 18), y + 58));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player)
    {
        return true;
    }

    @Override
    public ItemStack quickTransfer(PlayerEntity player, int fromIndex)
    {
        return ItemStack.EMPTY;
    }

    public boolean canActivate()
    {
        return this.baseSlot.hasStack() && (keySlot.hasStack() || reagent1Slot.hasStack() || reagent2Slot.hasStack());
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id)
    {
        if (!canActivate())
        {
            return false;
        }

        context.run((world, blockPos) -> {

            final ItemStack result = BifrostHelper.processKeyForging(world, baseSlot.getStack(), keySlot.getStack(), reagent1Slot.getStack(), reagent2Slot.getStack());

            Optional<AstralForgeCoreBlockEntity> opt = world.getBlockEntity(blockPos, ModBlockEntities.ASTRAL_FORGE_CORE_BLCOK_ENTITY);
            if (opt.isPresent())
            {
                opt.get().activate(result);
                baseSlot.setStack(ItemStack.EMPTY);
                keySlot.setStack(ItemStack.EMPTY);
                reagent1Slot.setStack(ItemStack.EMPTY);
                reagent2Slot.setStack(ItemStack.EMPTY);
            }
        });

        return false;
    }

    @Override
    public void close(PlayerEntity player)
    {
        super.close(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }
}
