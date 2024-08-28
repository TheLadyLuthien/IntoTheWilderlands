package com.skadoosh.minigame.items;

import net.minecraft.block.entity.StructureBlockBlockEntity.Action;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GraveTokenItem extends Item
{
    // @Override
    // public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    // {
    //     return redeem(world, user, hand) ? TypedActionResult.consume(user.getStackInHand(hand)) : TypedActionResult.success(user.getStackInHand(hand));
    // }

    public GraveTokenItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context)
    {
        return redeem(context.getWorld(), context.getPlayer(), context.getHand()) ? ActionResult.CONSUME : ActionResult.SUCCESS;
    }

    private boolean redeem(World world, PlayerEntity user, Hand hand)
    {
        if (world.isClient)
        {
            return false;
        }
        else
        {
            final var comp = user.getStackInHand(hand).get(DataComponentTypes.CONTAINER);
            
            final var inven = new SimpleInventory(54);
            comp.copyTo(inven.stacks);

            // Behavior here can be changed
            // shuch as to show an inventory etc in the future
            for (int i = 0; i < inven.stacks.size(); i++)
            {
                user.dropItem(inven.stacks.get(i), true, false);
            }

            user.setStackInHand(hand, ItemStack.EMPTY);
            
            return true;
        }
    }
}
