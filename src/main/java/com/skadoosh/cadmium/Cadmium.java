package com.skadoosh.cadmium;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Collection;

public final class Cadmium
{
    private static final HashMap<Identifier, AdvancedParticle> particles = new HashMap<>();
    public static void registerParticle(Identifier identifier, AdvancedParticle particle)
    {
        particles.put(identifier, particle);
    }

    public static AdvancedParticle retrieveParticle(Identifier identifier)
    {
        return particles.get(identifier);
    }

    public static Collection<AdvancedParticle> retrieveAllParticles()
    {
        return particles.values();
    }

    private Cadmium()
    {}

    public static void initialize()
    {
        PayloadTypeRegistry.playS2C().register(SummonParticlePayload.ID, SummonParticlePayload.CODEC);
        // ServerPlayNetworking.registerGlobalReceiver(C2S_DO_DOUBLEJUMP,
		// 	(server, player, handler, buf, responseSender) -> {
		// 		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		// 		passedData.writeUuid(buf.readUuid());

		// 		server.execute(() -> {
		// 			PlayerLookup.tracking(player).forEach(p -> {
		// 				if (p != player)
		// 				{
		// 					ServerPlayNetworking.send(p, InfinitySeasonThree.S2C_PLAY_EFFECTS_PACKET_ID, passedData);
		// 				}
		// 			});	
		// 		});
		// 	}
		// );
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient()
    {
        ParticleBuilder.clientsideRegisterParticles();

        ClientPlayNetworking.registerGlobalReceiver(SummonParticlePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                AdvancedParticle particle = retrieveParticle(payload.identifier());
                particle.doCreateParticle((ClientWorld)(context.player().getWorld()), payload.x(), payload.y(), payload.z(), payload.velocity().x, payload.velocity().y, payload.velocity().z);
            });
        });


        //  (client, handler, buf, responseSender) -> {
		// 	UUID effectPlayerUuid = buf.readUuid();
		// 	client.execute(() -> {
		// 		PlayerEntity effectPlayer = client.player.getEntityWorld().getPlayerByUuid(effectPlayerUuid);
		// 		if (effectPlayer != null)
		// 		{
		// 			DoubleJumpEffect.play(client.player, effectPlayer);
		// 		}
		// 	});
		// });
    }
}
