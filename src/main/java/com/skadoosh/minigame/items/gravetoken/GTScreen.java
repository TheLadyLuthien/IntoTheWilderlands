package com.skadoosh.minigame.items.gravetoken;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GTScreen extends HandledScreen<GTScreenHandler>
{
    private static final Identifier TEXTURE = Identifier.ofDefault("textures/gui/container/generic_54.png");
    private final int rows;

    public GTScreen(GTScreenHandler handler, PlayerInventory inventory, Text title)
    {
        super(handler, inventory, title);

        this.rows = handler.getRows();
        this.backgroundHeight = 114 + this.rows * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta)
    {
        super.render(graphics, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(GuiGraphics graphics, float delta, int mouseX, int mouseY)
    {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        graphics.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
        graphics.drawTexture(TEXTURE, i, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
    }
}

