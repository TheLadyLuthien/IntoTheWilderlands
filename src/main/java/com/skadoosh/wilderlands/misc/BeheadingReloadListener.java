package com.skadoosh.wilderlands.misc;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.skadoosh.wilderlands.Wilderlands;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public record BeheadingReloadListener(Provider wrapperLookup) implements SimpleSynchronousResourceReloadListener
{
	public static final Identifier ID = Wilderlands.id("beheading");

	@Override
	public Identifier getFabricId()
	{
		return ID;
	}

	@Override
	public void reload(ResourceManager manager)
	{
		BeheadingEntry.DROP_MAP.clear();
		manager.findAllResources("beheading", path -> path.getNamespace().equals(Wilderlands.NAMESPACE) && path.getPath().endsWith(".json")).forEach((identifier, resources) -> {
			for (Resource resource : resources)
			{
				try (InputStream stream = resource.open())
				{
					JsonObject object = JsonParser.parseReader(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();
					Identifier entityId = Identifier.parse(identifier.getPath().substring(identifier.getPath().indexOf("/") + 1, identifier.getPath().length() - 5).replace("/", ":"));
					EntityType<?> entity_type = Registries.ENTITY_TYPE.get(entityId);
					if (entity_type == Registries.ENTITY_TYPE.get(Registries.ENTITY_TYPE.getDefaultId()) && !entityId.equals(Registries.ENTITY_TYPE.getDefaultId()))
					{
						continue;
					}

					// Identifier dropId = Identifier.parse(JsonHelper.getString(object, "drop"));
					String dropInvStr = JsonHelper.getString(object, "drop");

					ItemStack stack = null;
					try
					{
						stack = ItemStack.fromNbt(this.wrapperLookup(), StringNbtReader.parse(dropInvStr));
					}
					catch (Exception e)
					{
						continue;
					}

					float chance = JsonHelper.getFloat(object, "chance");
					BeheadingEntry.DROP_MAP.put(entity_type, new BeheadingEntry(stack, chance));
				}
				catch (Exception ignored)
				{
				}
			}
		});
	}
}
