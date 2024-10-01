package com.skadoosh.wilderlands.screen.handler;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

@ClientOnly
public class AstralForgeCoreScreenHandler extends ScreenHandler
{

    public AstralForgeCoreScreenHandler(int syncId, Inventory inventory)
    {
        this(syncId, inventory, new ArrayPropertyDelegate(3), ScreenHandlerContext.EMPTY);
    }

    public AstralForgeCoreScreenHandler(int syncId, Inventory inventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context)
    {
        super(ModScreenHandlers.ASTRAL_FORGE_CORE_SCREEN_HANDLER_TYPE, syncId);
        checkDataCount(propertyDelegate, 3);
        // this.addSlot(this.paymentSlot);
        this.addProperties(propertyDelegate);
        

        int k;
        for (k = 0; k < 3; ++k)
        {
            for (int l = 0; l < 9; ++l)
            {
                this.addSlot(new Slot(inventory, l + k * 9 + 9, 36 + l * 18, 137 + k * 18));
            }
        }

        for (k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(inventory, k, 36 + k * 18, 195));
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
