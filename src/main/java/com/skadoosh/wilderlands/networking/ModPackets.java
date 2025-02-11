package com.skadoosh.wilderlands.networking;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.items.crossbow.BrimstoneCrossbowProjectileBehavior;
import com.skadoosh.wilderlands.networking.payload.BrimstoneHitPayload;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public class ModPackets
{
    public static final Identifier BRIMSTONE_HIT = Wilderlands.id("brimstone_hit");

    public static void init()
    {
        PayloadTypeRegistry.playS2C().register(BrimstoneHitPayload.ID, BrimstoneHitPayload.CODEC);
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit()
    {
        ClientPlayNetworking.registerGlobalReceiver(BrimstoneHitPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                BrimstoneCrossbowProjectileBehavior.spawnClientParticles(context.client(), payload.pos());
            });
        });
    }
}
