package com.skadoosh.wilderlands.persistance;

import java.util.HashMap;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class NamedKeystoneData implements AutoSyncedComponent
{
    public final HashMap<KeystoneLocation, String> map = new HashMap<>();

    public String get(Identifier dimension, BlockPos pos)
    {
        return map.get(new KeystoneLocation(dimension, pos));
    }

    public static final class KeystoneLocation
    {
        public final Identifier dimension;
        public final BlockPos position;

        public KeystoneLocation(Identifier dimension, BlockPos position)
        {
            this.dimension = dimension;
            this.position = position;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((dimension == null) ? 0 : dimension.hashCode());
            result = prime * result + ((position == null) ? 0 : position.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            KeystoneLocation other = (KeystoneLocation)obj;
            if (dimension == null)
            {
                if (other.dimension != null)
                    return false;
            }
            else if (!dimension.equals(other.dimension))
                return false;
            if (position == null)
            {
                if (other.position != null)
                    return false;
            }
            else if (!position.equals(other.position))
                return false;
            return true;
        }

        @Override
        public String toString()
        {
            return dimension + "/" + position.getX() + "/" + position.getY() + "/" + position.getZ();
        }

        public static KeystoneLocation fromString(String data)
        {
            try
            {
                String[] parts = data.split("/");
                Identifier id = Identifier.parse(parts[0]);

                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int z = Integer.parseInt(parts[3]);

                return new KeystoneLocation(id, new BlockPos(x, y, z));
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        for (String posData : tag.getKeys())
        {
            KeystoneLocation keystoneLocation = KeystoneLocation.fromString(posData);
            if (keystoneLocation != null)
            {
                map.put(keystoneLocation, tag.getString(posData));
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        for (KeystoneLocation pos : map.keySet())
        {
            tag.putString(pos.toString(), map.get(pos));
        }
    }
}
