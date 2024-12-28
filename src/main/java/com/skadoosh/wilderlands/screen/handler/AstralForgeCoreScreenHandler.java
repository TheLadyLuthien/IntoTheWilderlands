package com.skadoosh.wilderlands.screen.handler;

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

        baseSlot = new Slot(this.inventory, 0, 37, 35)
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

        reagent1Slot = new Slot(this.inventory, 2, 124, 12)
        {
            @Override
			public int getMaxItemCount()
            {
				return 1;
			}
        };
        reagent2Slot = new Slot(this.inventory, 3, 140, 12)
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
                this.addSlot(
                    new Slot(
                        playerInventory,
                        l + k * 9 + 9, // Index, +9 for hotbar
                        x + (l * 18),
                        y + (k * 18)
                    )
                );
            }
        }

        for (k = 0; k < 9; ++k)
        {
            this.addSlot(
                new Slot(
                    playerInventory,
                    k,
                    x + (k * 18),
                    y + 58
                )
            );
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
        return null;
    }
}
