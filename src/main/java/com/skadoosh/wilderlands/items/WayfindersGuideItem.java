package com.skadoosh.wilderlands.items;

import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.map.MapPlugin;

import journeymap.api.v2.common.waypoint.Waypoint;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class WayfindersGuideItem extends Item
{
    public WayfindersGuideItem(Item.Settings settings)
    {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context)
    {
        if (context.getWorld().getBlockState(context.getBlockPos()).isOf(ModBlocks.RUNIC_KEYSTONE))
        {
            // clicked on a keystone, add it to the map
            Waypoint wp = MapPlugin.createBifrostCircleWaypoint(context.getBlockPos(), context.getWorld().getRegistryKey());
        }
        return super.useOnBlock(context);
    }
}
