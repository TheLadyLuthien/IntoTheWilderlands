package com.skadoosh.wilderlands.entities;

import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.skadoosh.wilderlands.Wilderlands;
import com.skadoosh.wilderlands.entities.render.BifrostBeamEntityRenderer;
import com.skadoosh.wilderlands.misc.BifrostHelper;
import com.skadoosh.wilderlands.misc.ModChunkTickets;
import com.skadoosh.wilderlands.persistance.ModComponentKeys;
import com.skadoosh.wilderlands.persistance.NamedKeystoneData.KeystoneLocation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementFlag;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BifrostBeamEntity extends Entity
{
    public static final String ANIMATION_TICKS = "animation_ticks";
    public static final String DURATION = "duration";
    public static final String DESTINATION = "destination";
    public static final String DESTINATION_POS_X = "x";
    public static final String DESTINATION_POS_Y = "y";
    public static final String DESTINATION_POS_Z = "z";
    public static final String DESTINATION_WORLD = "world";
    public static final String HAS_TELEPORTED = "has_teleported";

    public static final int CHUNKLOADING_RADIUS = 3;

    public static final float TELEPORT_BOX_RADIUS = 6;
    
    // public static final int TELEPORT_COOLDOWN = 5 * 20;
    public static final int DEFAULT_DURATION = 6 * 20;

    private KeystoneLocation destination;

    public int duration;
    private int discardTime;
    private boolean hasTeleported = false;

    private int calcDiscardTime()
    {
        return (int)((BifrostBeamEntityRenderer.IN_TIME * 20f) + ((float)duration) + (BifrostBeamEntityRenderer.OUT_TIME * 20f));
    }

    private int animationTicks = 0;

    public int getAnimationTicks()
    {
        return animationTicks;
    }

    private int getTeleportationTick()
    {
        return (int)((BifrostBeamEntityRenderer.IN_TIME * 20f) + ((float)duration / 2f));
    }

    @Override
    public boolean shouldRender(double distance)
    {
        return true;
    }

    public BifrostBeamEntity(EntityType<? extends BifrostBeamEntity> entityType, World world)
    {
        this(entityType, world, DEFAULT_DURATION, new KeystoneLocation(Identifier.ofDefault("overworld"), BlockPos.ORIGIN));
    }

    public BifrostBeamEntity(EntityType<? extends BifrostBeamEntity> entityType, World world, int duration, KeystoneLocation destination)
    {
        super(entityType, world);
        this.duration = duration;
        this.destination = destination;
        this.discardTime = calcDiscardTime();
        this.ignoreCameraFrustum = true;

        if (world instanceof ServerWorld sw)
        {
            sw.getChunkManager().addTicket(ModChunkTickets.BIFROST, this.getChunkPos(), CHUNKLOADING_RADIUS, this.getChunkPos());
        }
    }

    public BifrostBeamEntity(World world, int duration, KeystoneLocation destination)
    {
        this(ModEntities.BIFROST_BEAM, world, duration, destination);
    }

    @Override
    public SoundCategory getSoundCategory()
    {
        return SoundCategory.MASTER;
    }

    @Override
    protected void initDataTracker(Builder builder)
    {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt)
    {
        animationTicks = nbt.getInt(ANIMATION_TICKS);
        hasTeleported = nbt.getBoolean(HAS_TELEPORTED);

        if (nbt.contains(DESTINATION))
        {
            NbtCompound compound = nbt.getCompound(DESTINATION);
            Identifier worlIdentifier = Identifier.parse(compound.getString(DESTINATION_WORLD));
            int x = compound.getInt(DESTINATION_POS_X);
            int y = compound.getInt(DESTINATION_POS_Y);
            int z = compound.getInt(DESTINATION_POS_Z);

            BlockPos blockPos = new BlockPos(x, y, z);
            this.destination = new KeystoneLocation(worlIdentifier, blockPos);
        }

        // keystoneLocaltion = nbt.contains(DURATION) ? nbt.getInt(DURATION) : duration;
        duration = nbt.contains(DURATION) ? nbt.getInt(DURATION) : duration;
        discardTime = calcDiscardTime();
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt)
    {
        nbt.putInt(ANIMATION_TICKS, animationTicks);
        nbt.putInt(DURATION, duration);
        nbt.putBoolean(HAS_TELEPORTED, hasTeleported);

        NbtCompound destCompount = new NbtCompound();
        destCompount.putString(DESTINATION_WORLD, destination.dimension.toString());
        destCompount.putInt(DESTINATION_POS_X, destination.position.getX());
        destCompount.putInt(DESTINATION_POS_Y, destination.position.getY());
        destCompount.putInt(DESTINATION_POS_Z, destination.position.getZ());

        nbt.put(DESTINATION, destCompount);
    }

    @Override
    public void tick()
    {
        super.tick();

        if ((this.animationTicks >= getTeleportationTick()) && !hasTeleported)
        {
            hasTeleported = true;

            if (this.getWorld() instanceof final ServerWorld serverWorld)
            {
                EnumSet<MovementFlag> set = EnumSet.noneOf(MovementFlag.class);
                set.add(MovementFlag.X_ROT);
                set.add(MovementFlag.Y_ROT);

                final var map = ModComponentKeys.NAMED_KEYSTONE_DATA.get(serverWorld.getServer().getOverworld());
                
                @Nullable
                final String keystoneName = map.get(this.destination.dimension, this.destination.position);

                final ServerWorld destinationWorld = serverWorld.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, destination.dimension));

                if (destinationWorld != null)
                {
                    Box teleportArea = new Box(new Vec3d(this.getX() - TELEPORT_BOX_RADIUS, this.getY() - TELEPORT_BOX_RADIUS, this.getZ() - TELEPORT_BOX_RADIUS), new Vec3d(this.getX() + TELEPORT_BOX_RADIUS, this.getY() + TELEPORT_BOX_RADIUS, this.getZ() + TELEPORT_BOX_RADIUS));
                    List<Entity> entities = serverWorld.getNonSpectatingEntities(Entity.class, teleportArea);
                    for (Entity entity : entities)
                    {
                        if (!canTeleportEntity(entity))
                        {
                            continue;
                        }

                        entity.teleport(destinationWorld, this.destination.position.getX() + 0.5, this.destination.position.getY() + 1.0, this.destination.position.getZ() + 0.5, set, entity.getYaw(), entity.getPitch());
                        entity.setNetherPortalCooldown(this.duration);

                        if ((entity instanceof ServerPlayerEntity player) && (keystoneName != null))
                        {
                            BifrostHelper.showTitleToPlayer(player, keystoneName);
                        }
                    }
                }
                else
                {
                    Wilderlands.LOGGER.error("Bifrost desstination dimension was null, id was " + destination.dimension.toString());
                }
            }
        }

        if (this.animationTicks >= discardTime)
        {
            this.discard();
        }

        this.animationTicks++;
    }

    public static boolean canTeleportEntity(Entity entity)
    {
        return (!(entity instanceof BifrostBeamEntity) && !entity.hasNetherPortalCooldown());
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ)
    {
        return true;
    }
}
