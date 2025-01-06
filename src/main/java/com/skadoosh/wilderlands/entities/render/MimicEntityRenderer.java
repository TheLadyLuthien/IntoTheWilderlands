// package com.skadoosh.wilderlands.entities.render;

// import java.util.Calendar;

// import org.quiltmc.loader.api.minecraft.ClientOnly;

// import com.mojang.blaze3d.vertex.VertexConsumer;
// import com.skadoosh.wilderlands.entities.MimicEntity;
// import com.skadoosh.wilderlands.entities.render.model.MimicEntityModel;

// import net.minecraft.block.AbstractChestBlock;
// import net.minecraft.block.BlockState;
// import net.minecraft.block.Blocks;
// import net.minecraft.block.ChestBlock;
// import net.minecraft.block.DoubleBlockProperties;
// import net.minecraft.block.entity.ChestBlockEntity;
// import net.minecraft.block.enums.ChestType;
// import net.minecraft.client.model.ModelData;
// import net.minecraft.client.model.ModelPart;
// import net.minecraft.client.model.ModelPartBuilder;
// import net.minecraft.client.model.ModelPartData;
// import net.minecraft.client.model.ModelTransform;
// import net.minecraft.client.model.TexturedModelData;
// import net.minecraft.client.render.RenderLayer;
// import net.minecraft.client.render.TexturedRenderLayers;
// import net.minecraft.client.render.VertexConsumerProvider;
// import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
// import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
// import net.minecraft.client.render.entity.EntityRendererFactory;
// import net.minecraft.client.render.entity.MobEntityRenderer;
// import net.minecraft.client.render.entity.model.EntityModelLayers;
// import net.minecraft.client.resource.Material;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.state.property.Property;
// import net.minecraft.util.Identifier;
// import net.minecraft.util.math.Axis;
// import net.minecraft.util.math.Direction;
// import net.minecraft.world.World;

// @Environment(EnvType.Client)
// public class MimicEntityRenderer extends MobEntityRenderer<MimicEntity, MimicEntityModel>
// {
//     public MimicEntityRenderer(EntityRendererFactory.Context ctx)
//     {
//         super(ctx, new MimicEntityModel(), 0.5f);

//         Calendar calendar = Calendar.getInstance();
// 		if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
// 			this.christmas = true;
// 		}

// 		ModelPart modelPart = ctx.getLayerModelPart(EntityModelLayers.CHEST);
// 		this.singleChestBase = modelPart.getChild("bottom");
// 		this.singleChestLid = modelPart.getChild("lid");
// 		this.singleChestLatch = modelPart.getChild("lock");
// 		// ModelPart modelPart2 = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_LEFT);
// 		// this.doubleChestRightBase = modelPart2.getChild("bottom");
// 		// this.doubleChestRightLid = modelPart2.getChild("lid");
// 		// this.doubleChestRightLatch = modelPart2.getChild("lock");
// 		// ModelPart modelPart3 = ctx.getLayerModelPart(EntityModelLayers.DOUBLE_CHEST_RIGHT);
// 		// this.doubleChestLeftBase = modelPart3.getChild("bottom");
// 		// this.doubleChestLeftLid = modelPart3.getChild("lid");
// 		// this.doubleChestLeftLatch = modelPart3.getChild("lock");
// 	}

//     public static TexturedModelData getSingleTexturedModelData()
//     {
//         ModelData modelData = new ModelData();
//         ModelPartData modelPartData = modelData.getRoot();
//         modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 19).cuboid(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), ModelTransform.NONE);
//         modelPartData.addChild("lid", ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), ModelTransform.pivot(0.0F, 9.0F, 1.0F));
//         modelPartData.addChild("lock", ModelPartBuilder.create().uv(0, 0).cuboid(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), ModelTransform.pivot(0.0F, 9.0F, 1.0F));
//         return TexturedModelData.of(modelData, 64, 64);
//     }

//     // public static TexturedModelData getRightDoubleTexturedModelData()
//     // {
//     //     ModelData modelData = new ModelData();
//     //     ModelPartData modelPartData = modelData.getRoot();
//     //     modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 19).cuboid(1.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F), ModelTransform.NONE);
//     //     modelPartData.addChild("lid", ModelPartBuilder.create().uv(0, 0).cuboid(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F), ModelTransform.pivot(0.0F, 9.0F, 1.0F));
//     //     modelPartData.addChild("lock", ModelPartBuilder.create().uv(0, 0).cuboid(15.0F, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F), ModelTransform.pivot(0.0F, 9.0F, 1.0F));
//     //     return TexturedModelData.of(modelData, 64, 64);
//     // }

//     // public static TexturedModelData getLeftDoubleTexturedModelData()
//     // {
//     //     ModelData modelData = new ModelData();
//     //     ModelPartData modelPartData = modelData.getRoot();
//     //     modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 19).cuboid(0.0F, 0.0F, 1.0F, 15.0F, 10.0F, 14.0F), ModelTransform.NONE);
//     //     modelPartData.addChild("lid", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F), ModelTransform.pivot(0.0F, 9.0F, 1.0F));
//     //     modelPartData.addChild("lock", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -2.0F, 14.0F, 1.0F, 4.0F, 1.0F), ModelTransform.pivot(0.0F, 9.0F, 1.0F));
//     //     return TexturedModelData.of(modelData, 64, 64);
//     // }

//     private void render(MatrixStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay)
//     {
//         lid.pitch = -(openFactor * (float)(Math.PI / 2));
//         latch.pitch = lid.pitch;
//         lid.render(matrices, vertices, light, overlay);
//         latch.render(matrices, vertices, light, overlay);
//         base.render(matrices, vertices, light, overlay);
//     }

//     @Override
//     public Identifier getTexture(MimicEntity entity)
//     {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'getTexture'");
//     }
// }
