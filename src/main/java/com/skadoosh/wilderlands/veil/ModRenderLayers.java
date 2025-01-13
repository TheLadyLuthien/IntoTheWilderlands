package com.skadoosh.wilderlands.veil;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.skadoosh.wilderlands.Wilderlands;

import foundry.veil.api.client.registry.RenderTypeStageRegistry;
import foundry.veil.api.client.render.VeilRenderBridge;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public final class ModRenderLayers extends RenderLayer {

    private static final Identifier CRYSTAL = Wilderlands.id("crystal");
    private static final Identifier BIFROST_BEAM = Wilderlands.id("bifrost_beam");

    // private static final BiFunction<Identifier, Boolean, RenderLayer> ENTITY_CUTOUT_NO_CULL = Util.memoize(((texture, outline) -> {
    //     RenderLayer.CompositeState compositeState = RenderLayer.CompositeState.builder()
    //             .setShaderState(RenderLayer_ENTITY_CUTOUT_NO_CULL_SHADER)
    //             .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
    //             .setTransparencyState(NO_TRANSPARENCY)
    //             .setCullState(NO_CULL)
    //             .setLightmapState(LIGHTMAP)
    //             .setOverlayState(OVERLAY)
    //             .createCompositeState(outline);
    //     RenderLayer RenderLayer = create("entity_tessellation_cutout_no_cull",
    //             DefaultVertexFormat.NEW_ENTITY,
    //             VertexFormat.Mode.QUADS,
    //             TRANSIENT_BUFFER_SIZE,
    //             true,
    //             false,
    //             compositeState);
    //     RenderLayerStageRegistry.addStage(RenderLayer, VeilRenderBridge.patchState(4));
    //     return RenderLayer;
    // }));

    private ModRenderLayers(String string, VertexFormat vertexFormat, VertexFormat.DrawMode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public static @Nullable RenderLayer crystal() {
        return VeilRenderType.get(CRYSTAL);
    }
    public static @Nullable RenderLayer bifrostBeam() {
        return VeilRenderType.get(BIFROST_BEAM);
    }

    // public static @Nullable RenderLayer heightmap(boolean tessellation) {
    //     return VeilRenderType.get(tessellation ? HEIGHTMAP_TESSELLATION : HEIGHTMAP_TEXTURE);
    // }
}
