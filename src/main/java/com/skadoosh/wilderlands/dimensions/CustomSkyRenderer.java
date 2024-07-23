package com.skadoosh.wilderlands.dimensions;

import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

@ClientOnly
public interface CustomSkyRenderer
{
    public abstract void renderSky(WorldRenderer wr, MatrixStack matrixStack, Matrix4f matrix4f, float tickDelta);
}
