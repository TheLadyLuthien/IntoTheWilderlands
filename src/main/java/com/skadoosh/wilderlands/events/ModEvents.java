package com.skadoosh.wilderlands.events;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents.StopSleeping;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public final class ModEvents
{
    public static void register()
    {
        EntitySleepEvents.STOP_SLEEPING.register(new StopSleeping() {
            @Override
            public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos)
            {
                entity.heal(entity.getMaxHealth());
            }
        });
    }
}
