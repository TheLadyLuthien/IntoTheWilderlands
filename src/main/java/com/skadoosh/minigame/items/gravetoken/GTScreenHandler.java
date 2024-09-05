package com.skadoosh.minigame.items.gravetoken;

import com.skadoosh.minigame.Minigame;
import com.skadoosh.minigame.blockentities.GravestoneBlockEntity;
import com.skadoosh.minigame.items.GraveTokenItem;

import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;

public class GTScreenHandler extends ScreenHandler
{
    private static final int SLOTS_PER_ROW = 9;
    private final ItemStack itemStack;
    private final Hand hand;
    private final PlayerInventory playerInventory;
    private final ScreenHandlerContext context;
    private final SimpleInventory inventory;
    private final int rows = 6;

    public int getRows()
    {
        return rows;
    }

    public GTScreenHandler(int syncId, PlayerInventory playerInventory)
    {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public GTScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context)
    {
        super(Minigame.GRAVE_TOKEN_SCREEN_HANDLER_TYPE, syncId);
        this.playerInventory = playerInventory;
        this.context = context;

        ItemStack stack = null;

        ItemStack mainHand = playerInventory.player.getStackInHand(Hand.MAIN_HAND);
        ItemStack offHand = playerInventory.player.getStackInHand(Hand.OFF_HAND);


        Hand h = null;
        if (mainHand != null && !mainHand.isEmpty())
        {
            stack = mainHand;
            h = Hand.MAIN_HAND;
        }
        else if (offHand != null && !offHand.isEmpty())
        {
            stack = offHand;
            h = Hand.OFF_HAND;
        }
        itemStack = stack;
        hand = h;


        ContainerContentsComponent comp = itemStack.get(DataComponentTypes.CONTAINER);
        inventory = new SimpleInventory(54);
        inventory.addListener(new InventoryChangedListener()
        {
            @Override
            public void onInventoryChanged(Inventory sender)
            {
                ContainerContentsComponent newComp = ContainerContentsComponent.fromStacks(inventory.stacks);
                GTScreenHandler.this.itemStack.set(DataComponentTypes.CONTAINER, newComp);
            }
        });
        comp.copyTo(inventory.stacks);

        /////////// slots ////////////
        int i = (rows - 4) * 18;

        int j;
        int k;
        for (j = 0; j < rows; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18)
                {
                    // NOTE: This is for my item. Remove for general use.
                    @Override
                    public boolean canInsert(ItemStack stack)
                    {
                        return false;
                    }
                });
            }
        }

        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i)
            {
                @Override
                public boolean canTakeItems(PlayerEntity playerEntity)
                {
                    if (this.getStack() == GTScreenHandler.this.itemStack)
                    {
                        return false;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean canUse(PlayerEntity player)
    {
        // TODO: make real
        return true;
    }

    @Override
    public ItemStack quickTransfer(PlayerEntity player, int fromIndex)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(fromIndex);
        if (slot != null && slot.hasStack())
        {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (fromIndex < this.rows * 9)
            {
                if (!this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                // TODO: fix!!!
                // the goal is to stop the player from moving items into the screen inventory
                // but it just crashes when I say return here???
                return ItemStack.EMPTY;
            }

            // Move from playerinv to gui inv
            // else if (!this.insertItem(itemStack2, 0, this.rows * 9, false))
            // {
            //     return ItemStack.EMPTY;
            // }

            if (itemStack2.isEmpty())
            {
                slot.method_53512(ItemStack.EMPTY);
            }
            else
            {
                slot.markDirty();
            }
        }

        return itemStack;
    }

    @Override
    public void close(PlayerEntity player)
    {
        super.close(player);
        if (inventory.isEmpty())
        {
            player.setStackInHand(hand, ItemStack.EMPTY);
        }
    }

}
