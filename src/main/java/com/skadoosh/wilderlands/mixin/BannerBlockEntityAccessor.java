package com.skadoosh.wilderlands.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.text.Text;

@Mixin(BannerBlockEntity.class)
public interface BannerBlockEntityAccessor
{
    @Accessor("customName")
    public void setCustomName(Text customName);
}
