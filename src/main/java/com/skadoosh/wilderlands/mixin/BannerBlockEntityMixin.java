package com.skadoosh.wilderlands.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.skadoosh.wilderlands.components.ModComponents;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.DataComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup;
import net.minecraft.util.math.BlockPos;

@Mixin(BannerBlockEntity.class)
public class BannerBlockEntityMixin extends BlockEntity
{
    public BannerBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
        throw new RuntimeException("Mixin constructor called!");
    }

    @Unique
    @Nullable
    public String teamId = null;

    private static final String NO_TEAM = "no_team";
    private static final String TEAM_ID = "team_id";

    @Inject(method = "writeNbt", at = @At("TAIL"))
    protected void writeNbt(NbtCompound nbt, HolderLookup.Provider lookupProvider, CallbackInfo ci)
    {
        // BannerBlockEntity thisEntity = (BannerBlockEntity)(Object)(this);
        nbt.putString(TEAM_ID, teamId == null ? NO_TEAM : teamId);
    }

    @Inject(method = "method_11014", at = @At("TAIL"))
    protected void readNbt(NbtCompound nbt, HolderLookup.Provider lookupProvider, CallbackInfo ci)
    {
        // BannerBlockEntity thisEntity = (BannerBlockEntity)(Object)(this);
        String id = nbt.getString(TEAM_ID);
        teamId = id.equals(NO_TEAM) ? null : id;
    }


    @Inject(method = "method_57568", at = @At("TAIL"))
    protected void getDataFromItem(BlockEntity.C_uyhxhbrv c_uyhxhbrv, CallbackInfo ci)
    {
        this.teamId = c_uyhxhbrv.method_58695(ModComponents.FLAG_TEAM_ID, (String)null);
    }

    // method_57567
    @Inject(method = "method_57567", at = @At("TAIL"))
    protected void setDataForItem(DataComponentMap.Builder builder, CallbackInfo ci)
    {
        builder.put(ModComponents.FLAG_TEAM_ID, teamId);
    }
}
