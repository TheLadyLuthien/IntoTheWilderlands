package com.skadoosh.wilderlands;

import com.skadoosh.cadmium.Cadmium;
import com.skadoosh.wilderlands.blocks.ModBlocks;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.entities.ModEntities;
import com.skadoosh.wilderlands.entities.render.TorchEntityRenderer;
import com.skadoosh.wilderlands.events.CoyoteBiteEvent;
import com.skadoosh.wilderlands.events.DashRenderEvent;
import com.skadoosh.wilderlands.events.LiftRenderEvent;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.misc.BifrostHelper;
import com.skadoosh.wilderlands.misc.BifrostHelper.KeyType;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClientLoader implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        Wilderlands.LOGGER.info("Wilderlands Client Loaded!");
        Cadmium.initializeClient();

        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipConfig, lines) -> {
            final NbtCompound nbt = BifrostHelper.getKeyComponent(stack);
            if (nbt != null)
            {
                final KeyType type = BifrostHelper.getKeyType(nbt);

                if (type == null)
                {
                    return;
                }

                switch (type)
                {
                    case TO_SINGLE_DESTINATION:
                        lines.add(1, Text.literal("To " + nbt.getString(BifrostHelper.NBT_KEYSTONE)));
                        break;

                    case TO_FROM_SINGLE_DESTINATION:
                        lines.add(1, Text.literal("To or from " + nbt.getString(BifrostHelper.NBT_KEYSTONE)));
                        break;

                    case TO_SINGLE_DIMENSION:
                        lines.add(1, Text.literal("To the ").append(BifrostHelper.getTranslatedDimension(nbt)));
                        break;

                    case WITHIN_CURRENT_DIMENSION:
                        lines.add(1, Text.literal("To anywhere within your current realm"));
                        break;

                    case UNIVERSAL:
                        lines.add(1, Text.literal("To anywhere"));
                        break;

                    default:
                        break;
                }

                int uses = nbt.getInt(BifrostHelper.NBT_USES);
                if (uses == -1)
                {
                    lines.add(2, Text.literal("Unlimited uses remaining"));
                }
                else
                {
                    lines.add(2, Text.literal(uses + " use" + (uses > 1 ? "s remaining" : " remaining")));
                }

                lines.add(3, Text.empty());

                if (!stack.isOf(ModItems.BIFROST_KEY))
                {
                    lines.add(1, Text.translatable("tooltip.bifrost_key.title"));
                }

                lines.add(1, Text.empty());
                
            }
        });

        ModelPredicateProviderRegistry.register(ModItems.BIFROST_KEY, Identifier.ofDefault("key_type"), (stack, world, entity, seed) -> {
            try
            {
                float val = (1f / (float)(KeyType.values().length)) * (float)BifrostHelper.getKeyType(BifrostHelper.getKeyComponent(stack)).ordinal();
                return val;
            }
            catch (Exception e)
            {
                return 0;
            }
        });

        ModelPredicateProviderRegistry.register(Items.BOW, Identifier.ofDefault("pull"), (stack, world, entity, seed) -> {
			if (entity == null) {
				return 0.0F;
			} else {
                var enchantment = world.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.QUICKDRAW);
                int level = EnchantmentHelper.getLevel(enchantment, stack);

                float time = 20f / ((float)level + 1f);
				return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getUseTicks(entity) - entity.getItemUseTimeLeft()) / time;
			}
		});

        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("blaze_rod"), (stack, world, entity, seed) -> {
			ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
			return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.BLAZE_ROD) ? 1.0F : 0.0F;
		});

        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("echo_shard"), (stack, world, entity, seed) -> {
			ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
			return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.ECHO_SHARD) ? 1.0F : 0.0F;
		});
        
        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("potion"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
			return chargedProjectilesComponent != null && (chargedProjectilesComponent.contains(Items.SPLASH_POTION) || chargedProjectilesComponent.contains(Items.LINGERING_POTION)) ? 1.0F : 0.0F;
		});

        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("ender_pearl"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.ENDER_PEARL) ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("dragons_breath"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.DRAGON_BREATH) ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("breeze_rod"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.BREEZE_ROD) ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("torch"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.TORCH) ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("brimstone"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(ModItems.BRIMSTONE_CARTRIDGE) ? 1.0F : 0.0F;
        });

        ModelPredicateProviderRegistry.register(Items.CROSSBOW, Wilderlands.id("bundle"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.BUNDLE) ? 1.0F : 0.0F;
        });

        HudRenderCallback.EVENT.register(new LiftRenderEvent());
        HudRenderCallback.EVENT.register(new DashRenderEvent());
        ClientTickEvents.END_WORLD_TICK.register(new CoyoteBiteEvent());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HOLOTILE, RenderLayer.getTranslucent());
    
        EntityRendererRegistry.register(ModEntities.TORCH, TorchEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BUNDLE, FlyingItemEntityRenderer::new);
    }
}
