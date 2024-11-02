
package com.skadoosh.wilderlands.events;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

@ClientOnly
public class CoyoteBiteEvent implements ClientTickEvents.EndWorldTick
{
    public static Entity target = null;
    public static int ticks = 0;

    public static final int MAX_TICKS = 3;

    @Override
    public void onEndTick(ClientWorld world)
    {
        if (MAX_TICKS > 0 && MinecraftClient.getInstance().targetedEntity != null)
        {
            target = MinecraftClient.getInstance().targetedEntity;
            ticks = MAX_TICKS;
        }
        if (ticks > 0)
        {
            ticks--;
        }
        if (ticks == 0 || target == null || target.isRemoved() || !target.isAlive())
        {
            target = null;
            ticks = 0;
        }
    }
}
