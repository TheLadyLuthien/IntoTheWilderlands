package com.skadoosh.cadmium;

import java.util.function.Supplier;

import com.skadoosh.cadmium.AdvancedParticle.RenderType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleBuilder
{
    private final Identifier identifier;

    private RenderType renderType = RenderType.PARTICLE_SHEET_OPAQUE;
    private boolean collidesWithWorld = false;
    private float scale = 1f;
    private boolean exactVelocity = false;

    private AdvancedParticle.RotationMode rotationMode = AdvancedParticle.RotationMode.ALIGN_WITH_CAMERA;

    private Supplier<Integer> lifetimeSupplier = () -> 1;

    public ParticleBuilder exactVelocity()
    {
        this.exactVelocity = true;
        return this;
    }

    public ParticleBuilder lifetime(final int lifetime)
    {
        lifetimeSupplier = () -> lifetime;
        return this;
    }
    public ParticleBuilder lifetime(Supplier<Integer> lifetimeSupplier)
    {
        this.lifetimeSupplier = lifetimeSupplier;
        return this;
    }

    public ParticleBuilder alignWithVelocity()
    {
        rotationMode = AdvancedParticle.RotationMode.ALIGN_WITH_VELOCITY;
        return this;
    }
    public ParticleBuilder alignWithWorldYAxis()
    {
        rotationMode = AdvancedParticle.RotationMode.ALIGN_WITH_WORLD;
        return this;
    }
    public ParticleBuilder alignWithCamera()
    {
        rotationMode = AdvancedParticle.RotationMode.ALIGN_WITH_CAMERA;
        return this;
    }

    public ParticleBuilder collidesWithWorld()
    {
        collidesWithWorld = true;
        return this;
    }
    
    public ParticleBuilder scale(float scale)
    {
        this.scale = scale;
        return this;    
    }

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

        AdvancedParticle particle = new AdvancedParticle(identifier, renderType, particleType, collidesWithWorld, scale, rotationMode, lifetimeSupplier, exactVelocity);

        Cadmium.registerParticle(identifier, particle);

        return particle;
    }

    @Environment(EnvType.CLIENT)
    public static void clientsideRegisterParticles()
    {
        for (AdvancedParticle advancedParticle : Cadmium.retrieveAllParticles())
        {
            ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
            registry.register(advancedParticle.getVanillaParticleType(), (fsp) -> advancedParticle.createParticleFactory(fsp));
        }
    }
}
