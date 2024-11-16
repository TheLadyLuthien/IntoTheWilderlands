// package com.skadoosh.wilderlands.entities.render.model;

// import com.mojang.blaze3d.vertex.VertexConsumer;
// import com.skadoosh.wilderlands.entities.MimicEntity;

// import net.minecraft.block.ChestBlock;
// import net.minecraft.block.DoubleBlockProperties;
// import net.minecraft.block.entity.ChestBlockEntity;
// import net.minecraft.block.enums.ChestType;
// import net.minecraft.client.model.ModelPart;
// import net.minecraft.client.render.RenderLayer;
// import net.minecraft.client.render.TexturedRenderLayers;
// import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
// import net.minecraft.client.render.entity.model.EntityModel;
// import net.minecraft.client.resource.Material;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.util.math.Axis;
// import net.minecraft.world.World;

// public class MimicEntityModel extends EntityModel<MimicEntity> {

//     private static final String BASE = "bottom";
//     private static final String LID = "lid";
//     private static final String LATCH = "lock";
//     private final ModelPart singleChestLid;
//     private final ModelPart singleChestBase;
//     private final ModelPart singleChestLatch;
//     private final ModelPart doubleChestRightLid;
//     private final ModelPart doubleChestRightBase;
//     private final ModelPart doubleChestRightLatch;
//     private final ModelPart doubleChestLeftLid;
//     private final ModelPart doubleChestLeftBase;
//     private final ModelPart doubleChestLeftLatch;
//     private boolean christmas;
    
//     @Override
//     public void setAngles(MimicEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
//     {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'setAngles'");
//     }

//     @Override
//     public void method_2828(MatrixStack matrices, VertexConsumer vertexConsumer, int i, int j, int k)
//     {
//         ChestType chestType = ChestType.SINGLE;
//         matrices.push();
//         // float f = ((Direction)blockState.get(ChestBlock.FACING)).asRotation();
//         matrices.translate(0.5F, 0.5F, 0.5F);
//         matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(-f));
//         matrices.translate(-0.5F, -0.5F, -0.5F);
//         // DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource;

//         // float g = propertySour\ce.apply(ChestBlock.getAnimationProgressRetriever(blockEntity)).get(tickDelta);
//         float g = 0; // anim progress

//         g = 1.0F - g;
//         g = 1.0F - g * g * g;
//         int i = propertySource.apply(new LightmapCoordinatesRetriever<>()).applyAsInt(light);
//         Material material = TexturedRenderLayers.getChestTexture(blockEntity, chestType, this.christmas);
//         VertexConsumer vertexConsumer = material.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
//         this.render(matrices, vertexConsumer, this.singleChestLid, this.singleChestLatch, this.singleChestBase, g, i, overlay);

//         matrices.pop();
//     }
    
// }
