package com.skadoosh.wilderlands.dimensions;

import org.joml.Matrix4f;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferRenderer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.mixin.WorldRendererAccessor;
import com.skadoosh.wilderlands.mixin.WorldRendererMixin;

import net.minecraft.client.render.DimensionVisualEffects;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.Vec3d;

public final class CustomDimensionVisualEffects
{
    @ClientOnly()
    public static class AstralWastes extends DimensionVisualEffects implements CustomSkyRenderer
    {
        public static final Identifier identifier = Wilderlands.id("astral_wastes");
        // public static final Identifier SKY_TEXTURE = Wilderlands.id("textures/environment/astral_wastes_skybox.png");
        public static final Identifier SKY_TEXTURE = Wilderlands.id("textures/environment/astral_wastes_skybox.png");

        public AstralWastes()
        {
            super(Float.NaN, true, DimensionVisualEffects.SkyType.NONE, false, false);
        }

        @Override
        public Vec3d adjustFogColor(Vec3d color, float sunHeight)
        {
            return color.multiply(0.0F);
        }

        @Override
        public boolean useThickFog(int camX, int camY)
        {
            return false;
        }

        @Override
        public void renderSky(WorldRenderer wr, MatrixStack matrices, Matrix4f unesedMatrix4f, float tickDelta)
        {
            // RenderSystem.enableBlend();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, SKY_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();

            for (int i = 0; i < 6; i++) {
                matrices.push();
                if (i == 1) {
                    matrices.rotate(Axis.X_POSITIVE.rotationDegrees(90.0F));
                }

                if (i == 2) {
                    matrices.rotate(Axis.X_POSITIVE.rotationDegrees(-90.0F));
                }

                if (i == 3) {
                    matrices.rotate(Axis.X_POSITIVE.rotationDegrees(180.0F));
                }

                if (i == 4) {
                    matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(90.0F));
                }

                if (i == 5) {
                    matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(-90.0F));
                }

                Matrix4f matrix4f = matrices.peek().getModel();
                BufferBuilder bufferBuilder = tessellator.method_60827(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

                final float count = 1;
                final int color = 0xffffffff;

                bufferBuilder.method_22918(matrix4f, -100.0F, -100.0F, -100.0F).method_22913(0.0F, 0.0F).method_39415(color);
                bufferBuilder.method_22918(matrix4f, -100.0F, -100.0F, 100.0F).method_22913(0.0F, count).method_39415(color);
                bufferBuilder.method_22918(matrix4f, 100.0F, -100.0F, 100.0F).method_22913(count, count).method_39415(color);
                bufferBuilder.method_22918(matrix4f, 100.0F, -100.0F, -100.0F).method_22913(count, 0.0F).method_39415(color);
                BufferRenderer.drawWithShader(bufferBuilder.method_60800());
                matrices.pop();
            }

            ((WorldRendererAccessor)(wr)).getStarsBuffer().bind();
            ((WorldRendererAccessor)(wr)).getStarsBuffer().draw(matrices.peek().getModel(), unesedMatrix4f, GameRenderer.getPositionShader());
            VertexBuffer.unbind();

            RenderSystem.depthMask(true);
            // RenderSystem.disableBlend();
        }
    }
}
