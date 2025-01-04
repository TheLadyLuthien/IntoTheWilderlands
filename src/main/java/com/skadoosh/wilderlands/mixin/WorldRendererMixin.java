package com.skadoosh.wilderlands.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.vertex.VertexBuffer;
import com.skadoosh.wilderlands.dimension.CustomSkyRenderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{
    @Shadow
    MinecraftClient client;

    // @Inject(
    //     method = "renderSky",
    //     at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER),
    //     locals = LocalCapture.CAPTURE_FAILHARD)
    // public void renderSky(Matrix4f projectionMatrix, Matrix4f matrix4f, float tickDelta, Camera preStep, boolean skipRendering, Runnable preRender, CallbackInfo ci, CameraSubmersionType cameraSubmersionType, MatrixStack matrixStack)
    // {
    //     WorldRenderer thisRenderer = (WorldRenderer)((Object)this);
    //     if (this.client.world.getSkyProperties() instanceof CustomSkyRenderer)
    //     {
    //         ((CustomSkyRenderer)this.client.world.getSkyProperties()).renderSky(thisRenderer, matrixStack, matrix4f, tickDelta);
    //     }
    // }

    @Accessor
    public abstract VertexBuffer getStarsBuffer();
}
