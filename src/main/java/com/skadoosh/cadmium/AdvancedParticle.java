package com.skadoosh.cadmium;

import org.joml.Vector3f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class AdvancedParticle
{
    public AdvancedParticle(Identifier identifier, RenderType renderType, DefaultParticleType vanillaParticleType)
    {
        this.identifier = identifier;
        this.renderType = renderType;
        this.vanillaParticleType = vanillaParticleType;
    }

    protected final Identifier identifier;
    protected final RenderType renderType;
    protected final DefaultParticleType vanillaParticleType;

    public static enum RenderType {
        TERRAIN_SHEET, PARTICLE_SHEET_OPAQUE, PARTICLE_SHEET_TRANSLUCENT, PARTICLE_SHEET_LIT, CUSTOM, NO_RENDER,
    }

    public DefaultParticleType getVanillaParticleType()
    {
        return vanillaParticleType;
    }

    @ClientOnly
    public Particle createVanillaParticle(DefaultParticleType arg0, ClientWorld arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7)
    {
        return this.new SummonableParticle(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    @ClientOnly
    public Factory createParticleFactory()
    {
        return this.new Factory();
    }

    public void spawn(ServerWorld world, float x, float y, float z, float velX, float velY, float velZ)
    {
        PlayerLookup.tracking(world, new BlockPos((int)x, (int)y, (int)z)).forEach(p -> {
            ServerPlayNetworking.send(p, new SummonParticlePayload(identifier, x, y, z, new Vector3f(velX, velY, velZ)));
        });
    }

    @ClientOnly
    public void spawn(ClientWorld world, float x, float y, float z, float velX, float velY, float velZ)
    {
        doCreateParticle(world, x, y, z, velX, velY, velZ);
    }

    @ClientOnly
    public void doCreateParticle(ClientWorld world, float x, float y, float z, float velX, float velY, float velZ)
    {
        world.addParticle(vanillaParticleType, x, y, z, velX, velY, velZ);
    }

    @ClientOnly
    public class SummonableParticle extends SpriteBillboardParticle
    {
        protected SummonableParticle(ClientWorld world, double d, double e, double f, double g, double h, double i)
        {
            super(world, d, e, f, g, h, i);
        }

        @Override
        public ParticleTextureSheet getType()
        {
            switch (AdvancedParticle.this.renderType)
            {
                case TERRAIN_SHEET:
                    return ParticleTextureSheet.TERRAIN_SHEET;
                case PARTICLE_SHEET_TRANSLUCENT:
                    return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
                case PARTICLE_SHEET_OPAQUE:
                    return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
                case PARTICLE_SHEET_LIT:
                    return ParticleTextureSheet.PARTICLE_SHEET_LIT;
                case NO_RENDER:
                    return ParticleTextureSheet.NO_RENDER;
                case CUSTOM:
                    return ParticleTextureSheet.CUSTOM;
            }
            return null;
        }
    }


    @ClientOnly
    public class Factory implements ParticleFactory<DefaultParticleType> 
    {
        protected Factory()
        {

        }

        @Override
        public Particle createParticle(DefaultParticleType arg0, ClientWorld arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7)
        {
            return AdvancedParticle.this.createVanillaParticle(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        }
    }

}
