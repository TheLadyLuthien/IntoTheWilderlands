package com.skadoosh.wilderlands.screen.renderer;

import java.util.List;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.screen.handler.AstralForgeCoreScreenHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.CyclingSlotBackground;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.button.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AstralForgeCoreScreen extends HandledScreen<AstralForgeCoreScreenHandler>
{
    private static final Identifier TEXTURE = Wilderlands.id("textures/gui/container/astral_forge_core.png");

    private static final Identifier HOE_ICON = Identifier.ofDefault("item/empty_slot_hoe");
    private static final Identifier AXE_ICON = Identifier.ofDefault("item/empty_slot_axe");
    private static final Identifier SWORD_ICON = Identifier.ofDefault("item/empty_slot_sword");
    private static final Identifier SHOVEL_ICON = Identifier.ofDefault("item/empty_slot_shovel");
    private static final Identifier PICKAXE_ICON = Identifier.ofDefault("item/empty_slot_pickaxe");

    private static final List<Identifier> KEY_SLOT_TEXTURES =
            List.of(Wilderlands.id("item/empty_slot/key_1"), Wilderlands.id("item/empty_slot/key_2"), Wilderlands.id("item/empty_slot/key_3"), Wilderlands.id("item/empty_slot/key_4"), Wilderlands.id("item/empty_slot/key_5"));
    private static final List<Identifier> BASE_SLOT_TEXTURES = List.of(HOE_ICON, AXE_ICON, SWORD_ICON, SHOVEL_ICON, PICKAXE_ICON);

    private final CyclingSlotBackground baseSlot = new CyclingSlotBackground(0);
    private final CyclingSlotBackground keySlot = new CyclingSlotBackground(1);

    public ButtonWidget button;

    public AstralForgeCoreScreen(AstralForgeCoreScreenHandler handler, PlayerInventory inventory, Text title)
    {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(GuiGraphics context, float delta, int mouseX, int mouseY)
    {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta)
    {
        super.render(graphics, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(graphics, mouseX, mouseY);
        this.baseSlot.render(this.handler, graphics, delta, this.x, this.y);
        this.keySlot.render(this.handler, graphics, delta, this.x, this.y);
    }


    @Override
    public void handledScreenTick()
    {
        super.handledScreenTick();
        this.baseSlot.tick(BASE_SLOT_TEXTURES);
        this.keySlot.tick(KEY_SLOT_TEXTURES);
    }

    @Override
    protected void init()
    {
        super.init();

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        button = ButtonWidget.builder(Text.literal("Activate"), button -> {
            // this.handler.activate();
            this.client.interactionManager.clickButton(this.handler.syncId, 0);
            // this.closeScreen();
        }).positionAndSize(x + 109, y + 56, 64, 18).build();


        this.addDrawableSelectableElement(button);  

        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
