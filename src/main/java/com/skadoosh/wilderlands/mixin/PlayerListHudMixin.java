package com.skadoosh.wilderlands.mixin;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

@ClientOnly
@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin
{
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract Text applyGameModeFormatting(PlayerListEntry entry, MutableText name);

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> ci)
    {
        GameProfile profile = entry.getProfile();
        if (client.world != null)
        {
            PlayerEntity player = client.world.getPlayerByUuid(profile.getId());

            if (player != null)
            {
                // final int lives = ModComponentKeys.GAME_PLAYER_DATA.get(player).getLives();
                ci.setReturnValue(applyGameModeFormatting(entry, player.getDisplayName().copy()));
                ci.cancel();
                return;
            }
        }
    }
}
