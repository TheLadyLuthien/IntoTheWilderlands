package com.skadoosh.wilderlands.entities.render;

import org.joml.Vector4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BuiltBuffer;
import com.skadoosh.mcutils.render.CylenderRenderer;
import com.skadoosh.wilderlands.entities.BifrostBeamEntity;
import com.skadoosh.wilderlands.veil.ModRenderLayers;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class BifrostBeamEntityRenderer extends EntityRenderer<BifrostBeamEntity>
{
    public BifrostBeamEntityRenderer(EntityRendererFactory.Context context)
    {
        super(context);
    }
    
    @Override
    public Identifier getTexture(BifrostBeamEntity entity)
    {
        return null;
    }
    
    private static BuiltBuffer generateMesh(MatrixStack matrices, int light, Vector4f customData)
    {
        return CylenderRenderer.render(2.5f, 600, 64, matrices, light, customData);
    }
    
    public static final float IN_TIME = 0.5f; // 1 sec in
    public static final float OUT_TIME = 0.5f; // 1 sec out
    
    private static float getBeamFullness(float tick, float duration)
    {
        final float time = tick / 20.0f;
        float partTime = time;
        if (time <= IN_TIME)
        {
            return ((-1 * (float)Math.pow((partTime / IN_TIME) - 1f, 2)) + 1);
        }
        else if ((partTime = time - IN_TIME) <= duration)
        {
            return 1;
        }
        else if ((partTime = time - IN_TIME - duration) <= OUT_TIME)
        {
            return ((-1 * ((partTime / OUT_TIME) * (partTime / OUT_TIME))) + 1);
        }

        // this is bad, do better
        return 0.5f;
    }

    @Override
    public void render(BifrostBeamEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
    {
        float tick = entity.getAnimationTicks() + tickDelta;

        final RenderLayer renderLayer = ModRenderLayers.bifrostBeam();
        if (renderLayer == null)
        {
            return;
        }

        matrices.push();

        // float beamPercent = map(MathHelper.sin(tick / 90.0f), -1, 1, 0, 1);
        // float beamPercent = (float)blockEntity.getBeamAnimationFrame().getPosition().x;
        float beamPercent = getBeamFullness(tick, ((float)entity.duration / 20f));

        final Vector4f customData = new Vector4f(beamPercent, 0, 0, 0);
        
        // matrices.scale(1, -1, 1);
        // matrices.rotate(new Quaternionf().rotationX(MathHelper.RADIANS_PER_DEGREE * 180));
        // matrices.rotate(new Quaternionf().rotationY(MathHelper.RADIANS_PER_DEGREE * 45));
        // matrices.mulPose(Axis.YN.rotationDegrees(facing.toYRot()));
        
        matrices.translate(0, -1, 0);
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        renderLayer.draw(generateMesh(matrices, light, customData));
        
        // matrices.scale(1, 1, 1);
        // matrices.translate(0, 0, 0);
        // renderLayer.draw(generateMesh(matrices, light, customData));
        
        matrices.pop();
    }

    @Override
    public boolean shouldRender(BifrostBeamEntity entity, Frustum frustum, double x, double y, double z)
    {
        return true;
    }
    
}
