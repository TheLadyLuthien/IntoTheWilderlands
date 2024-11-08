package com.skadoosh.wilderlands.entities.render;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.entities.TorchEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@ClientOnly
public class TorchEntityRenderer extends ProjectileEntityRenderer<TorchEntity>
{
    private static final Identifier TEXTURE = Wilderlands.id("textures/entity/projectiles/torch.png");

    public TorchEntityRenderer(EntityRendererFactory.Context context)
    {
        super(context);
    }
    
    @Override
    public Identifier getTexture(TorchEntity entity)
    {
        return TEXTURE;
    }
}
