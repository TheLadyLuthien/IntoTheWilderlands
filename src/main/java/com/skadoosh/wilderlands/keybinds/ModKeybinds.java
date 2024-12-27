package com.skadoosh.wilderlands.keybinds;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBind;

@Environment(EnvType.CLIENT)
public class ModKeybinds
{
    // public static KeyBind LIFT = KeyBindingHelper.registerKeyBinding(new KeyBind(
    //     "test",
    //     InputUtil.Type.KEYSYM,
    //     GLFW.GFLW_KEY_SPACE
    // ));
    public static void init()
    {
        
    }
}
