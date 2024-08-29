package com.skadoosh.wilderlands.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.skadoosh.minigame.ZoneHelper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.debug.DebugHud;

import java.util.List;

@ClientOnly
@Mixin(DebugHud.class)
public class DebugHudMixin
{
    @Shadow
    private MinecraftClient client;
    
    @Inject(method = "getLeftText", at = @At("TAIL"))
    protected void getLeftText(CallbackInfoReturnable<List<String>> ci, @Local List<String> list)
    {
        if (!this.client.hasReducedDebugInfo())
        {
            list.add("Current Zone Type: " + ZoneHelper.getZoneType(this.client.player));
        }
    }
}
