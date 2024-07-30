package com.skadoosh.wilderlands;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import com.skadoosh.cadmium.Cadmium;
import com.skadoosh.wilderlands.items.ModItems;
import com.skadoosh.wilderlands.misc.BifrostHelper;
import com.skadoosh.wilderlands.misc.BifrostHelper.KeyType;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ClientLoader implements ClientModInitializer
{
    @Override
    public void onInitializeClient(ModContainer mod)
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
    }
}
