package com.skadoosh.wilderlands.blockentities.render;

import java.util.List;

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
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import com.skadoosh.mcutils.render.CylenderRenderer;
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
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.ColorUtil;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.random.RandomGenerator;

@Environment(EnvType.CLIENT)
public class AstralForgeCoreBlockEntityRenderer implements BlockEntityRenderer<AstralForgeCoreBlockEntity>
{
    // private static final Identifier CRYSTAL_FRAMEBUFFER = Wilderlands.id("crystal");

    // private static final ModelIdentifier MODEL = ModelIdentifier.inventory(Wilderlands.id("astral_forge_anvil"));
    // private final MinecraftClient client;
    private final ModelPart modelPart = getTexturedModelData().createModel();
    // private static final ObjectSet<BlockPos> RENDER_POSITIONS = new ObjectArraySet<>();


    public AstralForgeCoreBlockEntityRenderer(BlockEntityRendererFactory.Context context)
    {
        // this.client = MinecraftClient.getInstance();
    }

    @Override
    public void render(AstralForgeCoreBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        RenderLayer renderLayer = ModRenderLayers.crystal();
        if (renderLayer == null)
        {
            return;
        }

        matrices.push();

        matrices.translate(0.5, 1.5 + 0.375, 0.5);
        matrices.rotate(new Quaternionf().rotationX(MathHelper.RADIANS_PER_DEGREE * 180));
        matrices.rotate(new Quaternionf().rotationY(MathHelper.RADIANS_PER_DEGREE * 45));

        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        modelPart.render(matrices, builder, light, overlay);
        
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderLayer.draw(builder.endOrThrow());
        
        matrices.pop();
    }

    public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -2.0F, -6.0F, 8.0F, 2.0F, 12.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-2.0F, -7.0F, -5.0F, 4.0F, 5.0F, 10.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-2.5F, -12.0F, -13.0F, 5.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-5.0F, -13.0F, -7.0F, 10.0F, 6.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 16, 16);
	}
}
