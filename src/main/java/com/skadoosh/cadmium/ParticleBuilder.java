package com.skadoosh.cadmium;

import java.util.HashMap;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.skadoosh.cadmium.AdvancedParticle.RenderType;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SweepAttackParticle;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class ParticleBuilder
{
    private final Identifier identifier;

    private RenderType renderType = RenderType.PARTICLE_SHEET_OPAQUE;
    
    public ParticleBuilder opaque()
    {
        renderType = RenderType.PARTICLE_SHEET_OPAQUE;
        return this;
    }
    
    public ParticleBuilder lit()
    {
        renderType = RenderType.PARTICLE_SHEET_LIT;
        return this;
    }
    
    public ParticleBuilder transluscent()
    {
        renderType = RenderType.PARTICLE_SHEET_TRANSLUCENT;
        return this;
    }

    public ParticleBuilder(Identifier identifier)
    {
        this.identifier = identifier;
    }

    public AdvancedParticle build()
    {
        DefaultParticleType particleType = Registry.register(Registries.PARTICLE_TYPE, this.identifier, FabricParticleTypes.simple());

        AdvancedParticle particle = new AdvancedParticle(identifier, renderType, particleType);

        Cadmium.registerParticle(identifier, particle);

        return particle;
    }

    @ClientOnly
    public static void clientsideRegisterParticles()
    {
        for (AdvancedParticle advancedParticle : Cadmium.retrieveAllParticles())
        {
            ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
            registry.register(advancedParticle.getVanillaParticleType(), advancedParticle.createParticleFactory());
        }
    }
}
