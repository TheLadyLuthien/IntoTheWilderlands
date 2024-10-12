package com.skadoosh.wilderlands.events;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.mojang.blaze3d.systems.RenderSystem;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.persistance.LiftComponent;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.DeltaTracker;
import net.minecraft.util.Identifier;

@ClientOnly
public class LiftRenderEvent implements HudRenderCallback
{
    private static final Identifier BACKGROUND = Wilderlands.id("hud/lift_background");
    private static final Identifier PROGRESS = Wilderlands.id("hud/lift_progress");

    @Override
    public void onHudRender(GuiGraphics drawContext, DeltaTracker tickCounter)
    {
        ModComponentKeys.LIFT.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(liftComponent -> {
            if (liftComponent.hasEnchantment())
            {
                if (liftComponent.shouldRenderIcon())
                {
                    RenderSystem.enableBlend();
                    int x = drawContext.getScaledWindowWidth() / 2 - 5, y = drawContext.getScaledWindowHeight() / 2 + 27;
                    if (liftComponent.getCooldown() > 0)
                    {
                        drawContext.drawGuiTexture(BACKGROUND, x, y, 9, 9);
                        drawContext.drawGuiTexture(PROGRESS, 9, 9, 0, 0, x, y, 9, (int)((liftComponent.getCooldown() / (float)LiftComponent.COOLDOWN) * 9));
                    }
                    else
                    {
                        drawContext.drawGuiTexture(BACKGROUND, x, y, 9, 9);
                    }
                    drawContext.setShaderColor(1, 1, 1, 1);
                    RenderSystem.disableBlend();
                }
            }
        });
    }

}
