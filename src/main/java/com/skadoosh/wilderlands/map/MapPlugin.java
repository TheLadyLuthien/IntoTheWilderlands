package com.skadoosh.wilderlands.map;

import com.skadoosh.wilderlands.Wilderlands;

import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.client.IClientPlugin;
import journeymap.api.v2.client.JourneyMapPlugin;
import journeymap.api.v2.common.waypoint.Waypoint;
import journeymap.api.v2.common.waypoint.WaypointFactory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Color;
import net.minecraft.util.CommonColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@JourneyMapPlugin(apiVersion = "2.0.0")
public class MapPlugin implements IClientPlugin
{
    private static IClientAPI jmApi = null;

    @Override
    public String getModId()
    {
        return Wilderlands.NAMESPACE;
    }

    @Override
    public void initialize(IClientAPI api)
    {
        jmApi = api;
    }

    public static Waypoint createBifrostCircleWaypoint(BlockPos location, RegistryKey<World> dimension)
    {
        Waypoint waypoint = null;
        try
        {
            waypoint = WaypointFactory.createClientWaypoint(Wilderlands.NAMESPACE, location, dimension, true);
            waypoint.setColor(CommonColors.BLUE);
            waypoint.setIconResourceLoctaion(Wilderlands.id("textures/map/bifrost_circle.png"));
            waypoint.setIconTextureSize(32, 32);
            waypoint.setIconColor(0x00ffff);
            // waypoint.

            // Add or update
            jmApi.addWaypoint(Wilderlands.NAMESPACE, waypoint);
        }
        catch (Throwable t)
        {
            Wilderlands.LOGGER.error(t.getMessage(), t);
        }
        return waypoint;
    }
}
