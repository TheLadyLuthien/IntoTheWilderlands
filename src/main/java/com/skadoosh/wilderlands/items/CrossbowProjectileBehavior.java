package com.skadoosh.wilderlands.items;

import java.util.HashMap;
import java.util.function.Predicate;

import com.skadoosh.wilderlands.components.ModComponents;
import com.skadoosh.wilderlands.enchantments.ModEnchantments;
import com.skadoosh.wilderlands.items.crossbow.ArrowCrossbowProjectileBehavior;

import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.world.World;

public abstract class CrossbowProjectileBehavior
{
    private static final HashMap<Item, CrossbowProjectileBehavior> REGISTERED_BEHAVIORS = new HashMap<>();
    private static final Predicate<ItemStack> IS_VALID_PROJECTILE = (stack) -> REGISTERED_BEHAVIORS.containsKey(stack.getItem());
    private static final CrossbowProjectileBehavior DEFAULT_BEHAVIOR = new ArrowCrossbowProjectileBehavior();

    public static void register(Item item, CrossbowProjectileBehavior behavior)
    {
        REGISTERED_BEHAVIORS.put(item, behavior);
    }

    public static CrossbowProjectileBehavior getBehavior(Item item)
    {
        return REGISTERED_BEHAVIORS.getOrDefault(item, DEFAULT_BEHAVIOR);
    }

    public static Predicate<ItemStack> isValidProjectile()
    {
        return IS_VALID_PROJECTILE;
    }

    private static void setPrefrencItemStack(ItemStack weapon, ItemStack prefrence, Provider provider)
    {
        NbtCompound comp = new NbtCompound();
        comp.put("stack", prefrence.encode(provider));
        
        weapon.set(ModComponents.CROSSBOW_PREFRENCE, NbtComponent.of(comp)); 
    }

    public static ItemStack getArrowItemStack(final LivingEntity entity, ItemStack weapon)
    {
        if (weapon.getItem() instanceof CrossbowItem item)
        {
            Predicate<ItemStack> predicate = item.getProjectiles().and(stack -> getBehavior(stack.getItem()).allowLoading(entity, weapon));
            DynamicRegistryManager provider = entity.getRegistryManager();
            
            ItemStack overrideItemStack = RangedWeaponItem.getHeldProjectile(entity, predicate);
            if (!overrideItemStack.isEmpty() && predicate.test(overrideItemStack))
            {
                setPrefrencItemStack(weapon, overrideItemStack, provider);
                return overrideItemStack; 
            }

            if (entity instanceof PlayerEntity player)
            {
                ItemStack temp = ItemStack.EMPTY;
                if (weapon.get(ModComponents.CROSSBOW_PREFRENCE) != null)
                {
                    NbtCompound comp = weapon.get(ModComponents.CROSSBOW_PREFRENCE).copy();
                    temp = ItemStack.fromNbt(provider, comp.getCompound("stack"));
                }
                final ItemStack weaponPrefrence = temp;
    
                // search the rest of the inventory
                Predicate<ItemStack> combined = predicate.and((stack) -> (
                    weaponPrefrence.isEmpty() ||
                    ItemStack.itemsAndComponentsMatch(stack, weaponPrefrence)
                ));
    
                for (ItemStack stack : player.getInventory().main)
                {
                    if (combined.test(stack))
                    {
                        setPrefrencItemStack(weapon, stack, provider);
                        return stack;
                    }
                }
    
                return player.getAbilities().creativeMode ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
            else
            {
                return entity.getArrowType(weapon);
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    public void applyRecoil(LivingEntity user, ItemStack weapon)
    {
        if (user.isSneaking() || EnchantmentHelper.getLevel(user.getRegistryManager().getLookupOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(ModEnchantments.RESILIENT), weapon) > 0)
        {
            return;
        }
        user.addVelocity(user.getRotationVector().normalize().multiply(-1 * getRecoil()));
    }

    public int getUseDamage()
    {
        return 1;
    }

    public float getLaunchSpeed()
    {
        return 3.0f;
    }

    public float getRecoil()
    {
        return 0.3f;
    }

    public boolean allowLoading(LivingEntity user, ItemStack crossbow)
    {
        return true;
    }

    public abstract ProjectileEntity getProjectileEntity(World world, LivingEntity entity, ItemStack weapon, ItemStack arrow, boolean isCritical);
}
