package com.skadoosh.wilderlands.datagen.tag;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.skadoosh.mcutils.datagen.annotations.DamageTypeTag;
import com.skadoosh.mcutils.datagen.annotations.DamageTypeTag.VanillaDamageTags;
import com.skadoosh.wilderlands.damage.ModDamageTypes;
import com.skadoosh.wilderlands.misc.AnnotationHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModDamageTypeTags extends FabricTagProvider<DamageType>
{
    public ModDamageTypeTags(FabricDataOutput output, CompletableFuture<Provider> completableFuture)
    {
        super(output, RegistryKeys.DAMAGE_TYPE, completableFuture);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void configure(Provider wrapperLookup)
    {
        ArrayList<AnnotationHelper.ValueAnnotationPair<RegistryKey, DamageTypeTag>> pairs = AnnotationHelper.getFieldsWithAnnotation(DamageTypeTag.class, ModDamageTypes.class, RegistryKey.class);
        for (var pair : pairs)
        {
            for (VanillaDamageTags tag : pair.annotation.value())
            {
                TagKey<DamageType> key = TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.ofDefault(tag.id));
                var builder = getOrCreateTagBuilder(key);
                builder.addOptional(pair.value);
            }
        }
    }
}