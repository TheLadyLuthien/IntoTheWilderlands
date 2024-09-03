package com.skadoosh.wilderlands.mixin;

import java.util.Iterator;
import java.util.List;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.skadoosh.wilderlands.components.ModComponents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@ClientOnly
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
    @Inject(method = "hasOutline", at = @At("RETURN"), cancellable = true)
    public void hasOutline(Entity entity, CallbackInfoReturnable<Boolean> ci)
    {
        if (entity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity)(entity);
            Iterator<DefaultedList<ItemStack>> var2 = ((PlayerInventoryAccessor)(player.getInventory())).getCombinedInventory().iterator();

            while (var2.hasNext())
            {
                List<ItemStack> list = (List<ItemStack>)var2.next();
                Iterator<ItemStack> var4 = list.iterator();

                while (var4.hasNext())
                {
                    ItemStack itemStack = (ItemStack)var4.next();
                    String teamId = itemStack.get(ModComponents.FLAG_TEAM_ID);
                    if (!itemStack.isEmpty() && !(teamId == null || teamId.trim().isEmpty()))
                    {
                        ci.setReturnValue(true);
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }
}
