package com.skadoosh.wilderlands.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com.skadoosh.wilderlands.misc.BifrostHelper.KeyType;

public abstract class AstralForgeEvent
{
    public final int cost;

    public AstralForgeEvent(int mishapCost)
    {
        this.cost = mishapCost;
    }

    public abstract void apply(KeyValues key);

    public static class KeyValues
    {
        public KeyType type;
        public int usesRemaining;
        public String dimension;
        public String fixedDestination;

        public KeyValues(KeyType type, int usesRemaining, String dimension, String fixedDestination)
        {
            this.type = type;
            this.usesRemaining = usesRemaining;
            this.dimension = dimension;
            this.fixedDestination = fixedDestination;
        }
    }
    
    private static final KeyType[] keyTypeValues = KeyType.values();

    public static final Set<AstralForgeEvent> UPGRADE_EVENTS = createUpgrades();

    public static void apply(KeyValues key, int points, Set<AstralForgeEvent> eventSet)
    {
        var list = new ArrayList<>(eventSet.stream().toList());
        Collections.shuffle(list);

        for (var event : list)
        {
            // do we have the points?
            if (points - event.cost >= 0)
            {
                event.apply(key);
                points -= event.cost;
            }
        }
    }

    private static Set<AstralForgeEvent> createUpgrades()
    {
        return Set.of(
            new AstralForgeEvent(20) 
            {
                @Override
                public void apply(KeyValues key)
                {
                    key.type = keyTypeValues[Math.min(key.type.ordinal() + 1, (keyTypeValues.length - 1))];
                }
            },
            new AstralForgeEvent(30) 
            {
                @Override
                public void apply(KeyValues key)
                {
                    key.type = keyTypeValues[Math.min(key.type.ordinal() + 1, (keyTypeValues.length - 1))];
                }
            },
            new AstralForgeEvent(4) 
            {
                @Override
                public void apply(KeyValues key)
                {
                    if (key.usesRemaining > 0)
                    {
                        key.usesRemaining += 3;
                    }
                }
            },
            new AstralForgeEvent(60) 
            {
                @Override
                public void apply(KeyValues key)
                {
                    key.type = KeyType.UNIVERSAL;
                }
            },
            new AstralForgeEvent(50) 
            {
                @Override
                public void apply(KeyValues key)
                {
                    key.usesRemaining = -1;
                }
            }
        );
    }

    // public static AstralForgeEvent[] createMishaps()
    // {
        
    // }
}
