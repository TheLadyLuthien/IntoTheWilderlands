package com.skadoosh.wilderlands.blockentities;

import org.jetbrains.annotations.Nullable;

import com.skadoosh.cadmium.animation.AnimationStep;
import com.skadoosh.cadmium.animation.ParticleAnimation;
import com.skadoosh.wilderlands.screen.handler.AstralForgeCoreScreenHandler;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.skadoosh.cadmium.animation.AnimatedValue.constant;
import static com.skadoosh.cadmium.animation.AnimatedValue.linear;

public class AstralForgeCoreBlockEntity extends BlockEntity implements NamedScreenHandlerFactory
{
    public static final String FORGING_PLAYING = "forging_playing";

    @Nullable
    private ItemStack finishedItem = null;

    public AstralForgeCoreBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.ASTRAL_FORGE_CORE_BLCOK_ENTITY, pos, state);
    }

    @Override
    public ScreenHandler createMenu(int arg0, PlayerInventory arg1, PlayerEntity arg2)
    {
        return new AstralForgeCoreScreenHandler(arg0, arg1, new ArrayPropertyDelegate(3), ScreenHandlerContext.create(this.world, this.getPos()));
    }

    @Override
    public Text getDisplayName()
    {
        return Text.literal("Astral Forge");
    }

    private final ParticleAnimation forgingAnimation = new ParticleAnimation(
        AnimationStep.particle(
            40,
            ParticleTypes.ENCHANT,
            constant(center().x),
            linear(center().y + 0.7, center().y + 2.6),
            constant(center().z),
            constant(0), constant(0.02f), constant(0),
            linear(1, 30), linear(1, 10)
        ),
        AnimationStep.delay(50),
        AnimationStep.multi(3, 
            AnimationStep.particle(
                3,
                ParticleTypes.ENCHANTED_HIT,
                constant(center().x),
                constant(center().y + 1.5),
                constant(center().z),
                constant(0), constant(0), constant(0),
                constant(20), constant(1)
            ),
            AnimationStep.particle(
                3,
                ParticleTypes.LAVA,
                constant(center().x),
                constant(center().y + 1.5),
                constant(center().z),
                constant(0), constant(0), constant(0),
                constant(1), constant(1)
            )
        ),
        AnimationStep.event(world -> {
            ItemEntity item = new ItemEntity(world, center().x, center().y + 1.5, center().z, finishedItem, 0, 0.3, 0);
            world.spawnEntity(item);
        })
    );

    public static void tick(World world, BlockPos pos, BlockState state, AstralForgeCoreBlockEntity blockEntity)
    {
        blockEntity.forgingAnimation.tick(world);
    }

    // runs server side only
    public void activate(ItemStack result)
    {
        this.forgingAnimation.play();
        this.finishedItem = result;
    }

    public Vec3d center()
    {
        return new Vec3d(this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5);
    }

    // @Override
    // protected void readNbtImpl(NbtCompound nbt, Provider lookupProvider)
    // {
    //     super.readNbtImpl(nbt, lookupProvider);
    //     forgingAnimation.setPlaying(nbt.getBoolean(FORGING_PLAYING));
    // }

    // // @Nullable
    // // @Override
    // // public Packet<ClientPlayPacketListener> toUpdatePacket()
    // // {
    // //     return BlockEntityUpdateS2CPacket.create(this);
    // // }

    // @Override
    // protected void writeNbt(NbtCompound nbt, Provider lookupProvider)
    // {
    //     super.writeNbt(nbt, lookupProvider);
    //     nbt.putBoolean(FORGING_PLAYING, forgingAnimation.playing());
    // }

    // @Override
    // public BlockEntityUpdateS2CPacket toUpdatePacket()
    // {
    //     return BlockEntityUpdateS2CPacket.of(this);
    // }

    // @Override
    // public NbtCompound toSyncedNbt(Provider lookupProvider)
    // {
    //     return this.toNbt(lookupProvider);
    // }
}
