package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.skadoosh.wilderlands.persistance.DashComponent;
import com.skadoosh.wilderlands.persistance.LiftComponent;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
    private boolean jumpedLastTick = false;
    private boolean snuckLastTick = false;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tickMovement(CallbackInfo info)
    {
        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;

        LiftComponent lift = ModComponentKeys.LIFT.get(player);
        if (!jumpedLastTick && player.input.jumping && lift.canUse())
        {
            lift.trigger();
        }

        DashComponent dash = ModComponentKeys.DASH.get(player);
        if (!snuckLastTick && player.input.sneaking && dash.canUse())
        {
            dash.trigger();
        }

        final boolean onGround = player.isOnGround();
        jumpedLastTick = player.input.jumping || onGround;
        snuckLastTick = player.input.sneaking || onGround;
    }
}
