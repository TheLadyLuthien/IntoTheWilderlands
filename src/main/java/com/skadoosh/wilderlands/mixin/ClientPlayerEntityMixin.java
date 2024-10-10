package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.skadoosh.wilderlands.persistance.LiftComponent;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
    private boolean jumpedLastTick = false;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tickMovement(CallbackInfo info)
    {
        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;

        LiftComponent lift = ModComponentKeys.LIFT.get(player);

        if (!jumpedLastTick && player.input.jumping && lift.canUse())
        {
            lift.trigger();
        }

        jumpedLastTick = player.input.jumping;
    }
}
