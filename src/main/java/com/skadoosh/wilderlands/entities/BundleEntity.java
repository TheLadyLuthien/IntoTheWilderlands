package com.skadoosh.wilderlands.entities;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class BundleEntity extends ThrownItemEntity
{
    public BundleEntity(EntityType<? extends ThrownItemEntity> entityType, World world)
    {
        super(entityType, world);
    }

    public BundleEntity(World world, LivingEntity owner)
    {
        super(ModEntities.BUNDLE, owner, world); // null will be changed later
    }

    public BundleEntity(World world, double x, double y, double z)
    {
        super(ModEntities.BUNDLE, x, y, z, world); // null will be changed later
    }

    @Override
    protected Item getDefaultItem()
    {
        return Items.BUNDLE;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult)
    {
        onHit();
    }
    
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult)
    {
        onHit();
    }

    private void onHit()
    {
        BundleContentsComponent bcc = this.getStack().getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
        ItemScatterer.spawn(this.getWorld(), this.getBlockPos().add(0, 1, 0), DefaultedList.copyOf(ItemStack.EMPTY, bcc.streamCopiedContents().toArray(ItemStack[]::new)));
        this.kill();
    }
}
