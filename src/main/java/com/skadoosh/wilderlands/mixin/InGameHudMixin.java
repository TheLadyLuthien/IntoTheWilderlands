package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.SmolderingComponent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.in_game.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
    // @Unique
    // private static final Identifier BATRACHOTOXIN_HEARTS = Wilderlands.id("textures/gui/flame_heart.png");
    // new Identifier("pickyourpoison", "textures/gui/batrachotoxin_hearts.png");
//     @Unique
//     private static final Identifier TORPOR_HEARTS = new Identifier("pickyourpoison", "textures/gui/torpor_hearts.png");
//     @Unique
//     private static final Identifier NUMBNESS_HEARTS = new Identifier("pickyourpoison", "textures/gui/numbness_hearts.png");

    // public InGameHudMixin(MinecraftClient client)
    // {
    //     super(client);
    // }

    // @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    // private void drawCustomHeart(GuiGraphics context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci)
    // {
    //     SmolderingComponent component;
    //     if (!blinking && type == InGameHud.HeartType.NORMAL && MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player
    //             && ((component = ModComponentKeys.SMOLDERING.get(player)).hasEnchantment()) && component.hasCharge())
    //     {
    //     //     Identifier textureId;
    //     //     if (player.hasStatusEffect(PickYourPoison.TORPOR))
    //     //     {
    //     //         textureId = TORPOR_HEARTS;
    //     //     }
    //     //     else if (player.hasStatusEffect(PickYourPoison.BATRACHOTOXIN))
    //     //     {
    //     //         textureId = BATRACHOTOXIN_HEARTS;
    //     //     }
    //     //     else if (player.hasStatusEffect(PickYourPoison.NUMBNESS))
    //     //     {
    //     //         textureId = NUMBNESS_HEARTS;
    //     //     }
    //     //     else
    //     //     {
    //     //         return;
    //     //     }
    //     //     context.drawTexture(textureId, x, y, halfHeart ? 9 : 0, v, 9, 9);
    //     //     ci.cancel();
    //     }
    // }
}
