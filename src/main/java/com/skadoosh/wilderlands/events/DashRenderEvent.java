package com.skadoosh.wilderlands.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.persistance.DashComponent;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.DeltaTracker;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DashRenderEvent implements HudRenderCallback
{
    private static final Identifier BACKGROUND = Wilderlands.id("hud/dash_background");
    private static final Identifier PROGRESS = Wilderlands.id("hud/dash_progress");

    @Override
    public void onHudRender(GuiGraphics drawContext, DeltaTracker tickCounter)
    {
        ModComponentKeys.DASH.maybeGet(MinecraftClient.getInstance().cameraEntity).ifPresent(dashComponent -> {
            if (dashComponent.hasEnchantment())
            {
                if (dashComponent.shouldRenderIcon())
                {
                    RenderSystem.enableBlend();

                    int x = drawContext.getScaledWindowWidth() / 2 - 5, y = drawContext.getScaledWindowHeight() / 2 + 18;

                    if (dashComponent.getCooldown() > 0)
                    {
                        drawContext.drawGuiTexture(BACKGROUND, x, y, 10, 4);
                        drawContext.drawGuiTexture(PROGRESS, 10, 4, 0, 0, x, y, 10 - (int)((dashComponent.getCooldown() / (float)DashComponent.COOLDOWN) * 10), 4);
                    }
                    else
                    {
                        drawContext.drawGuiTexture(BACKGROUND, x, y, 10, 4);
                    }

                    drawContext.setShaderColor(1, 1, 1, 1);
                    RenderSystem.disableBlend();
                }
            }
        });
    }

}
