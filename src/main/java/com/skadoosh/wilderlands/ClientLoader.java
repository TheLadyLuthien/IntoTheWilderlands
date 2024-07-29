package com.skadoosh.wilderlands;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import com.skadoosh.cadmium.Cadmium;
import com.skadoosh.wilderlands.misc.BifrostHelper;
import com.skadoosh.wilderlands.misc.BifrostHelper.KeyType;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

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
                lines.add(1, Text.translatable("tooltip.bifrost_key.title"));
                
                final KeyType type = BifrostHelper.getKeyType(nbt);

                if (type == null)
                {
                    return;
                }

                switch (type) {
                    case TO_SINGLE_DESTINATION:
                        lines.add(2, Text.literal("To §o" + nbt.getString(BifrostHelper.NBT_KEYSTONE)));
                    break;
                        
                    case TO_FROM_SINGLE_DESTINATION:
                        lines.add(2, Text.literal("To or from §o" + nbt.getString(BifrostHelper.NBT_KEYSTONE)));
                    break;
                
                    case TO_SINGLE_DIMENSION:
                        lines.add(2, Text.literal("To the §o").append(BifrostHelper.getTranslatedDimension(nbt)));
                    break;
                
                    case WITHIN_CURRENT_DIMENSION:
                        lines.add(2, Text.literal("To §oanywhere within your current realm"));
                    break;
                
                    case UNIVERSAL:
                        lines.add(2, Text.literal("To §oanywhere"));
                    break;

                    default:
                    break;
                }

                int uses = nbt.getInt(BifrostHelper.NBT_USES);
                if (uses == -1)
                {
                    lines.add(3, Text.literal("Unlimited uses remaining"));
                }
                else
                {
                    lines.add(3, Text.literal(uses + " use" + (uses > 1 ? "s remaining" : " remaining")));
                }
                
                lines.add(4, Text.empty());
                lines.add(1, Text.empty());
			}
		});
    }
}
