package com.skadoosh.wilderlands.networking.payload;

import org.joml.Vector3f;

import com.skadoosh.wilderlands.networking.ModPackets;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.payload.CustomPayload;

public record BrimstoneHitPayload(Vector3f pos) implements CustomPayload
{
    public static final CustomPayload.Id<BrimstoneHitPayload> ID = new CustomPayload.Id<>(ModPackets.BRIMSTONE_HIT);
    public static final PacketCodec<RegistryByteBuf, BrimstoneHitPayload> CODEC = PacketCodec.tuple(PacketCodecs.VECTOR3F, BrimstoneHitPayload::pos, BrimstoneHitPayload::new);

    @Override
    public Id<? extends CustomPayload> getId()
    {
        return ID;
    }
}
