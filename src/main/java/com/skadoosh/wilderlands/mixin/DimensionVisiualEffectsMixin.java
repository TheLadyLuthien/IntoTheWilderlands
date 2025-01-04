package com.skadoosh.wilderlands.mixin;

import net.minecraft.client.render.DimensionVisualEffects;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.util.Util;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.skadoosh.wilderlands.dimension.CustomDimensionVisualEffects;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

@Mixin(DimensionVisualEffects.class)
public class DimensionVisiualEffectsMixin
{
    private static final Object2ObjectMap<Identifier, DimensionVisualEffects> CUSTOM_BY_IDENTIFIER = Util.make(new Object2ObjectArrayMap<>(), map -> {
		DimensionVisualEffects.Overworld overworld = new DimensionVisualEffects.Overworld();
		
        map.defaultReturnValue(overworld);

		map.put(DimensionTypes.OVERWORLD_ID, overworld);
		map.put(DimensionTypes.THE_NETHER_ID, new DimensionVisualEffects.Nether());
		map.put(DimensionTypes.THE_END_ID, new DimensionVisualEffects.End());
		
        map.put(CustomDimensionVisualEffects.AstralWastes.identifier, new CustomDimensionVisualEffects.AstralWastes());
	});

    @Inject(method = "byDimensionType", at = @At("HEAD"), cancellable = true)
    private static void byDimensionType(DimensionType dimensionType, CallbackInfoReturnable<DimensionVisualEffects> ci)
    {
        ci.setReturnValue(CUSTOM_BY_IDENTIFIER.get(dimensionType.effectsLocation()));
        ci.cancel(); return;
    }
}
