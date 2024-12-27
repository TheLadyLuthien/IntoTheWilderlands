package com.skadoosh.cadmium;

import java.util.function.Supplier;

import org.joml.Vector3f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AdvancedParticle
{
    public AdvancedParticle(Identifier identifier, RenderType renderType, DefaultParticleType vanillaParticleType, boolean collidesWithWorld, float scale, RotationMode rotationMode, Supplier<Integer> lifetimeSupplier, boolean exactVelocity)
    {
        this.identifier = identifier;
        this.renderType = renderType;
        this.vanillaParticleType = vanillaParticleType;
        this.collidesWithWorld = collidesWithWorld;
        this.scale = scale;
        this.rotationMode = rotationMode;
        this.lifetimeSupplier = lifetimeSupplier;
        this.exactVelocity = exactVelocity;
    }

    protected final Identifier identifier;
    protected final RenderType renderType;
    protected final DefaultParticleType vanillaParticleType;
    protected final boolean collidesWithWorld;
    protected final float scale;
    protected final AdvancedParticle.RotationMode rotationMode;
    protected final Supplier<Integer> lifetimeSupplier;
    protected final boolean exactVelocity;

    public static enum RotationMode {
        ALIGN_WITH_CAMERA, ALIGN_WITH_WORLD, ALIGN_WITH_VELOCITY;
    }

    public static enum RenderType {
        TERRAIN_SHEET, PARTICLE_SHEET_OPAQUE, PARTICLE_SHEET_TRANSLUCENT, PARTICLE_SHEET_LIT, CUSTOM, NO_RENDER,
    }

    public DefaultParticleType getVanillaParticleType()
    {
        return vanillaParticleType;
    }

    @Environment(EnvType.CLIENT)
    public Particle createVanillaParticle(DefaultParticleType arg0, ClientWorld arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7, SpriteProvider spriteProvider)
    {
        return this.new SummonableParticle(arg1, arg2, arg3, arg4, arg5, arg6, arg7, spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public Factory createParticleFactory(SpriteProvider spriteProvider)
    {
        return this.new Factory(spriteProvider);
    }

    public void spawn(ServerWorld world, float x, float y, float z, float velX, float velY, float velZ)
    {
        PlayerLookup.tracking(world, new BlockPos((int)x, (int)y, (int)z)).forEach(p -> {
            ServerPlayNetworking.send(p, new SummonParticlePayload(identifier, x, y, z, new Vector3f(velX, velY, velZ)));
        });
    }

    @Environment(EnvType.CLIENT)
    public void spawn(ClientWorld world, float x, float y, float z, float velX, float velY, float velZ)
    {
        doCreateParticle(world, x, y, z, velX, velY, velZ);
    }

    @Environment(EnvType.CLIENT)
    public void doCreateParticle(ClientWorld world, float x, float y, float z, float velX, float velY, float velZ)
    {
        world.addParticle(vanillaParticleType, x, y, z, velX, velY, velZ);
    }

    @Environment(EnvType.CLIENT)
    public class SummonableParticle extends SpriteBillboardParticle
    {
        protected final SpriteProvider spriteProvider;
        protected final double initialVelocityX;
        protected final double initialVelocityY;
        protected final double initialVelocityZ;

        private final BillboardParticle.FacingCameraMode velocityAlignmentFunction;

        protected SummonableParticle(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ, SpriteProvider spriteProvider)
        {
            super(world, x, y, z, velX, velY, velZ);

            this.spriteProvider = spriteProvider;
            this.setSpriteForAge(spriteProvider);

            if (AdvancedParticle.this.exactVelocity)
            {
                this.velocityX = velX;
                this.velocityY = velY;
                this.velocityZ = velZ;
            }

            this.collidesWithWorld = AdvancedParticle.this.collidesWithWorld;
            this.scale = AdvancedParticle.this.scale;
            this.maxAge = AdvancedParticle.this.lifetimeSupplier.get();

            this.initialVelocityX = velX;
            this.initialVelocityY = velY;
            this.initialVelocityZ = velZ;
            this.velocityAlignmentFunction = (rotation, camera, tickDelta) -> {
                rotation.fromAxisAngleRad(0, 1, 0, (float)MathHelper.atan2(initialVelocityZ, initialVelocityX));
                // rotation.set((float)(-initialVelocityZ), camera.getRotation().y, (float)(initialVelocityX), 0.0f);
            };
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

        @Override
        public BillboardParticle.FacingCameraMode getFacingCameraMode()
        {
            switch (rotationMode)
            {
                case ALIGN_WITH_CAMERA:
                    return FacingCameraMode.ALL_AXIS;
                case ALIGN_WITH_WORLD:
                    return FacingCameraMode.Y_AND_W;
                case ALIGN_WITH_VELOCITY:
                    return this.velocityAlignmentFunction;

                default:
                    return super.getFacingCameraMode();
            }
        }

        @Override
        public void tick()
        {
            super.tick();
            this.setSpriteForAge(this.spriteProvider);
        }
    }


    @Environment(EnvType.CLIENT)
    public class Factory implements ParticleFactory<DefaultParticleType>
    {
        protected final SpriteProvider spriteProvider;

        protected Factory(SpriteProvider spriteProvider)
        {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType arg0, ClientWorld arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7)
        {
            return AdvancedParticle.this.createVanillaParticle(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, spriteProvider);
        }
    }
}
