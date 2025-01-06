package com.skadoosh.wilderlands.dimension;

import org.joml.Matrix4f;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public interface CustomSkyRenderer
{
    public abstract void renderSky(WorldRenderer wr, MatrixStack matrixStack, Matrix4f matrix4f, float tickDelta);
}
