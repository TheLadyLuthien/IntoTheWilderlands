package com.skadoosh.wilderlands.blockentities.render;

import org.joml.Vector4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.skadoosh.mcutils.render.CylenderRenderer;
import com.skadoosh.wilderlands.blockentities.AstralForgeCoreBlockEntity;
import com.skadoosh.wilderlands.blockentities.CarvedRunestoneBlockEntity;
import com.skadoosh.wilderlands.veil.ModRenderLayers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class CarvedRunestoneBlockEntityRenderer implements BlockEntityRenderer<CarvedRunestoneBlockEntity>
{
    public CarvedRunestoneBlockEntityRenderer(BlockEntityRendererFactory.Context context)
    {

    }

    private float map(float value,float min1,float max1,float min2,float max2){
        return min2+(value-min1)*(max2-min2)/(max1-min1);
    }

    @Override
    public void render(CarvedRunestoneBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        float tick = (float)(blockEntity.getTicks()) + tickDelta;

        final RenderLayer renderLayer = ModRenderLayers.bifrostBeam();
        if (renderLayer == null)
        {
            return;
        }

        matrices.push();

        float beamPercent = map(MathHelper.sin(tick / 90.0f), -1, 1, 0, 1);

        final Vector4f customData = new Vector4f(beamPercent, 0, 0, 0);
        
        
        // matrices.scale(1, -1, 1);
        // matrices.rotate(new Quaternionf().rotationX(MathHelper.RADIANS_PER_DEGREE * 180));
        // matrices.rotate(new Quaternionf().rotationY(MathHelper.RADIANS_PER_DEGREE * 45));
        // matrices.mulPose(Axis.YN.rotationDegrees(facing.toYRot()));
        matrices.translate(0, -1, 0);
        
        BlockPos difference = blockEntity.getKeystonePos().subtract(blockEntity.getPos());
        matrices.translate(difference.getX(), difference.getY(), difference.getZ());
        
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderLayer.draw(CylenderRenderer.render(2.5f, 600, 64, matrices, light, customData));
        
        matrices.pop();
    }
}
