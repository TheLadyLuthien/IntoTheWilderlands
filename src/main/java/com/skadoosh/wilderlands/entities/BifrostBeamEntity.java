package com.skadoosh.wilderlands.entities;

import com.skadoosh.wilderlands.entities.render.BifrostBeamEntityRenderer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class BifrostBeamEntity extends Entity
{
    private static final String ANIMATION_TICKS = "animation_ticks";
    private static final String DURATION = "duration";
    
    public int duration;
    private int discardTime;

    private int calcDiscardTime()
    {
        return (int)((BifrostBeamEntityRenderer.IN_TIME * 20f) + ((float)duration) + (BifrostBeamEntityRenderer.OUT_TIME * 20f));
    }
    
    private int animationTicks = 0;

    public int getAnimationTicks()
    {
        return animationTicks;
    }

    @Override
    public boolean shouldRender(double distance)
    {
        return true;
    }

    public BifrostBeamEntity(EntityType<? extends BifrostBeamEntity> entityType, World world)
    {
        this(entityType, world, 20 * 4);
    }

    public BifrostBeamEntity(EntityType<? extends BifrostBeamEntity> entityType, World world, int duration)
    {
        super(entityType, world);
        this.duration = duration;
        this.discardTime = calcDiscardTime();
        this.ignoreCameraFrustum = true;
    }

    public BifrostBeamEntity(World world, int duration)
    {
        this(ModEntities.BIFROST_BEAM, world, duration);
    }


    @Override
    public SoundCategory getSoundCategory()
    {
        return SoundCategory.MASTER;
    }

    @Override
    protected void initDataTracker(Builder builder)
    {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt)
    {
        animationTicks = nbt.getInt(ANIMATION_TICKS);
        
        duration = nbt.contains(DURATION) ? nbt.getInt(DURATION) : duration;
        discardTime = calcDiscardTime();
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt)
    {
        nbt.putInt(ANIMATION_TICKS, animationTicks);
        nbt.putInt(DURATION, duration);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.animationTicks >= discardTime)
        {
            this.discard();
        }

        this.animationTicks++;
    }
}
