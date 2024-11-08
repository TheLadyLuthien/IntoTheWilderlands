package com.skadoosh.mcutils.datagen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DamageTypeTag
{
    public enum VanillaDamageTags
    {
        NO_KNOCKBACK("no_knockback"),
        BYPASSES_ENCHANTMENTS("bypasses_enchantments"),
        BYPASSES_INVULNERABILITY("bypasses_invulnerability"),
        BYPASSES_COOLDOWN("bypasses_cooldown"),
        BYPASSES_SHIELD("bypasses_shield"),
        BYPASSES_ARMOR("bypasses_armor"),
        BYPASSES_RESISTANCE("bypasses_resistance");

        public final String id;

        private VanillaDamageTags(String id)
        {
            this.id = id;
        }
    }

    public VanillaDamageTags[] value();
}
