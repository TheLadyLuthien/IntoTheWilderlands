package com.skadoosh.wilderlands.mixin;

import java.util.UUID;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.skadoosh.minigame.DeathHelper;
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
        if (client.world != null)
        {
            GameProfile profile = entry.getProfile();
            UUID uuid = profile.getId();
            int lives = ModComponentKeys.GAME_CLIENT_LIVES_DATA.get(client.world.getScoreboard()).getLives(uuid);
            ci.setReturnValue(applyGameModeFormatting(entry, Text.literal(entry.getProfile().getName()).formatted(DeathHelper.getNameColor(lives))));
            ci.cancel();
            return;
        }
    }
}
