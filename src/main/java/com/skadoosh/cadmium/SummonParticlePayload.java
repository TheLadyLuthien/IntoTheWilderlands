package com.skadoosh.cadmium;

import org.joml.Vector3f;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.util.Identifier;

public record SummonParticlePayload(Identifier identifier, float x, float y, float z, Vector3f velocity) implements CustomPayload
{
    private static final Identifier S2C_SUMMON_PARTICLE = Identifier.of("cadmium", "summon_particle");
    public static final CustomPayload.Id<SummonParticlePayload> ID = new CustomPayload.Id<>(S2C_SUMMON_PARTICLE);
    public static final PacketCodec<RegistryByteBuf, SummonParticlePayload> CODEC = PacketCodec.tuple(
        Identifier.PACKET_CODEC, SummonParticlePayload::identifier,
        PacketCodecs.FLOAT, SummonParticlePayload::x,
        PacketCodecs.FLOAT, SummonParticlePayload::y,
        PacketCodecs.FLOAT, SummonParticlePayload::z,
        PacketCodecs.VECTOR3F, SummonParticlePayload::velocity,
        SummonParticlePayload::new);
    
    // should you need to send more data, add the appropriate record parameters and change your codec:
    // public static final PacketCodec<RegistryByteBuf, BlockHighlightPayload> CODEC =
    // PacketCodec.tuple(
    // BlockPos.PACKET_CODEC, BlockHighlightPayload::blockPos,
    // PacketCodecs.INTEGER, BlockHighlightPayload::myInt,
    // Uuids.PACKET_CODEC, BlockHighlightPayload::myUuid,
    // BlockHighlightPayload::new
    // );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId()
    {
        return ID;
    }
}
