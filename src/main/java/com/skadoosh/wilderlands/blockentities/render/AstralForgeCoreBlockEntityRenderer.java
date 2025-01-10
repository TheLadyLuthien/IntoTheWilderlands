package com.skadoosh.wilderlands.blockentities.render;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.NativeResource;

import com.mojang.blaze3d.glfw.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.blockentities.AstralForgeCoreBlockEntity;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.veil.ModRenderLayers;

import foundry.veil.api.client.render.CullFrustum;
import foundry.veil.api.client.render.VeilLevelPerspectiveRenderer;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.framebuffer.AdvancedFbo;
import foundry.veil.api.client.render.framebuffer.FramebufferManager;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.DeltaTracker;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Environment(EnvType.CLIENT)
public class AstralForgeCoreBlockEntityRenderer implements BlockEntityRenderer<AstralForgeCoreBlockEntity>, NativeResource
{
    private static final Identifier CRYSTAL_FRAMEBUFFER = Wilderlands.id("crystal");

    // private static final ObjectSet<BlockPos> RENDER_POSITIONS = new ObjectArraySet<>();

    public AstralForgeCoreBlockEntityRenderer(BlockEntityRendererFactory.Context context)
    {}

    @Override
    public void free()
    {}

    @Override
    public void render(AstralForgeCoreBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        if (VeilLevelPerspectiveRenderer.isRenderingPerspective())
        {
            return;
        }

        // BlockPos blockPos = blockEntity.getPos();
        // RENDER_POSITIONS.add(blockPos.toImmutable());

        // MirrorTexture texture = TEXTURES.get(getKey(pos, facing));
        // if (texture == null) {
        // return;
        // }

        RenderLayer renderLayer = ModRenderLayers.crystal();
        if (renderLayer == null)
        {
            return;
        }

        matrices.push();
        // matrices.translate(0.5, 0.5, 0.5);
        matrices.translate(0, 0, 1.5);
        // matrices.mulPose(Axis.YN.rotationDegrees(facing.toYRot()));
        // matrices.translate(-0.5, -0.5, -0.5);

        Matrix4f pos = matrices.peek().getModel();
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        builder.xyz(pos, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv0(0.0F, 0.0F).uv2(light).normal(0, 0, 1);
        builder.xyz(pos, 1, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv0(1.0F, 0.0F).uv2(light).normal(0, 0, 1);
        builder.xyz(pos, 1, 1, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv0(1.0F, 1.0F).uv2(light).normal(0, 0, 1);
        builder.xyz(pos, 0, 1, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv0(0.0F, 1.0F).uv2(light).normal(0, 0, 1);

        // RenderSystem.setShaderTexture(0, texture.getId());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderLayer.draw(builder.endOrThrow());

        matrices.pop();
    }

    // public static void renderLevel(ClientWorld world, Matrix4fc projection, DeltaTracker deltaTracker, CullFrustum frustum, Camera camera)
    // {
    //     if (VeilLevelPerspectiveRenderer.isRenderingPerspective() || !projection.isFinite())
    //     {
    //         return;
    //     }

    //     FramebufferManager framebufferManager = VeilRenderSystem.renderer().getFramebufferManager();
    //     AdvancedFbo fbo = framebufferManager.getFramebuffer(CRYSTAL_FRAMEBUFFER);
    //     if (fbo == null)
    //     {
    //         return;
    //     }

    //     Vector3dc cameraPos = frustum.getPosition();

    //     // Vector3d renderPos = new Vector3d(pos.getX() + 0.5 - normal.getX() * 0.375, pos.getY() + 0.5 - normal.getY() * 0.375, pos.getZ() + 0.5 - normal.getZ() * 0.375);
    //     // Vector3f offset = new Vector3f((float)(cameraPos.x() - renderPos.x), (float)(cameraPos.y() - renderPos.y), (float)(cameraPos.z() - renderPos.z));
    //     // Vector4f plane = new Vector4f(normal.getX(), normal.getY(), normal.getZ(), -offset.dot(normal.getX(), normal.getY(), normal.getZ()));

    //     Window window = MinecraftClient.getInstance().getWindow();
    //     float aspect = (float)window.getWidth() / window.getHeight();
    //     float fov = projection.perspectiveFov();
    //     RENDER_PROJECTION.setPerspective(fov, aspect, 0.3F, RENDER_DISTANCE * 4);

    //     offset.reflect(normal.getX(), normal.getY(), normal.getZ());
    //     renderPos.add(offset);

    //     Vector3f dir = camera.getLookVector().reflect(normal.getX(), normal.getY(), normal.getZ(), new Vector3f());
    //     Vector3f up = camera.getUpVector().reflect(normal.getX(), normal.getY(), normal.getZ(), new Vector3f());

    //     new Quaternionf().lookAlong(dir, up).transform(plane);

    //     calculateObliqueMatrix(RENDER_PROJECTION, plane, RENDER_PROJECTION);

        
    //     // VeilLevelPerspectiveRenderer.render
    //     // VeilLevelPerspectiveRenderer.render(fbo, RENDER_MODELVIEW, RENDER_PROJECTION, renderPos, VIEW.identity().lookAlong(dir, up), RENDER_DISTANCE, deltaTracker);
    // }
}
