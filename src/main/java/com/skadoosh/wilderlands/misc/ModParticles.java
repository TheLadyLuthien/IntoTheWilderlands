package com.skadoosh.wilderlands.misc;

import com.skadoosh.cadmium.AdvancedParticle;
import com.skadoosh.cadmium.ParticleBuilder;
import com.skadoosh.wilderlands.Wilderlands;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;

public final class ModParticles
{
    private static final RandomGenerator RANDOM = RandomGenerator.createThreaded();
    
    public static final AdvancedParticle RUNESTONE_IDLE = new ParticleBuilder(Wilderlands.id("runestone_idle")).opaque().alignWithWorldYAxis().scale(0.3f).lifetime(() -> Math.max((int)(8.0F / MathHelper.nextBetween(RANDOM, 0.5f, 1.0f) * 1f), 1)).exactVelocity().build();
    public static final AdvancedParticle BIFROST_BEAM = new ParticleBuilder(Wilderlands.id("bifrost_beam")).opaque().alignWithWorldYAxis().scale(1.9f).lifetime(() -> Math.max((int)(MathHelper.nextBetween(RANDOM, 16f, 20f)), 1)).exactVelocity().build();
    // public static final AdvancedParticle KEYSTONE_ACTIVATE = new ParticleBuilder(Wilderlands.id("keystone_activate")).transluscent().alignWithVelocity().scale(1).collidesWithWorld().lifetime(16).exactVelocity().build();

    public static void init()
    {

    }
}
