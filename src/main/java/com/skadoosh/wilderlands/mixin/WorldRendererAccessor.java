package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.vertex.VertexBuffer;

import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor
{
    @Accessor
    VertexBuffer getStarsBuffer();
}
