package com.skadoosh.mcutils.render;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BuiltBuffer;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public final class CylenderRenderer
{
    public static BuiltBuffer render(float radius, float height, int divisions, MatrixStack matrices, int light, Vector4f vertexColor)
    {
        Entry entry = matrices.peek();
        Matrix4f pose = entry.getModel();
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);

        final float angleIncrement = (2.0f * (float)Math.PI) / (float)divisions;
        for (int i = 0; i < divisions + 1; i++)
        {
            final int index = i % divisions;
            final float angle = (index * angleIncrement);

            final float zOffset = (float)((radius) * Math.sin(angle));
            final float xOffset = (float)((radius) * Math.cos(angle));

            Vec3d normal = new Vec3d(1, 0, 0).rotateY(-angle);

            float u = (float)i / (float)divisions;

            // u = (1 - Math.abs((2 * u) - 1));

            builder.xyz(pose, xOffset, 0, zOffset).color(vertexColor.x, vertexColor.y, vertexColor.z, vertexColor.w).uv0(u, 0.0f).uv2(light).normal(entry, (float)normal.x, (float)normal.y, (float)normal.z);
            builder.xyz(pose, xOffset, height, zOffset).color(vertexColor.x, vertexColor.y, vertexColor.z, vertexColor.w).uv0(u, 1.0f).uv2(light).normal(entry, (float)normal.x, (float)normal.y, (float)normal.z);
        }

        return builder.endOrThrow();
    }
}
