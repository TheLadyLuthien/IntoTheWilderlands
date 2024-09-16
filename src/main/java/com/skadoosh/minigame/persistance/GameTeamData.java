package com.skadoosh.minigame.persistance;

import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import com.skadoosh.minigame.RaidHelper;
import com.skadoosh.minigame.TeamRefrence;
import com.skadoosh.minigame.voicechat.VoicehcatHelper;
import com.skadoosh.wilderlands.persistance.NbtValue;

import de.maxhenkel.voicechat.api.Group;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.SoundPlayS2CPacket;
import net.minecraft.registry.Holder;
import net.minecraft.registry.HolderLookup.Provider;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GameTeamData implements ServerTickingComponent
{
    public static final int RAID_DURATION = 20 * 60 * 20;

    private final Team team;
    private final Scoreboard scoreboard;
    private final MinecraftServer server;

    private final NbtValue<Integer> flagCaptureCount = new NbtValue<Integer>("flag_capture_count", 0);
    private final NbtValue<Integer> markedKills = new NbtValue<Integer>("marked_kills", 0);
    private final NbtValue<Boolean> hasEverstar = new NbtValue<Boolean>("has_everstar", false);

    private final NbtValue<Long> tick = new NbtValue<Long>("tick", 0l);

    public final NbtValue<Long> nextRaidStart = new NbtValue<Long>("next_raid_start", (long)(-RAID_DURATION));
    public final NbtValue<Long> previousRaidStart = new NbtValue<Long>("prev_raid_start", (long)(-RAID_DURATION));

    private final NbtWorldPosValue baseLocation = new NbtWorldPosValue("base_location", new BlockPos(0, 0, 0), Identifier.ofDefault("overworld"));

    private static final Holder<StatusEffect>[] RAID_EFFECTS = new Holder[] {StatusEffects.STRENGTH, StatusEffects.SPEED, StatusEffects.INVISIBILITY, StatusEffects.WEAVING, StatusEffects.FIRE_RESISTANCE, StatusEffects.ABSORPTION};

    @Nullable
    private Group voiceGroup = null;

    public Group getVoiceGroup()
    {
        return voiceGroup;
    }

    public long getTick()
    {
        return tick.get();
    }

    public void setVoiceGroup(Group voiceGroup)
    {
        this.voiceGroup = voiceGroup;
    }

    public NbtWorldPosValue getBaseLocation()
    {
        return baseLocation;
    }

    public int getTotalScore()
    {
        return flagCaptureCount.get() + markedKills.get() + (hasEverstar.get() ? 3 : 0);
    }

    public void addCapture()
    {
        flagCaptureCount.set(flagCaptureCount.get() + 2);
    }

    public void addEverstar()
    {
        hasEverstar.set(true);
    }

    public void addKill()
    {
        markedKills.set(markedKills.get() + 1);
    }

    public GameTeamData(Team team, Scoreboard scoreboard, @Nullable MinecraftServer server)
    {
        this.team = team;
        this.scoreboard = scoreboard;
        this.server = server;
    }

    @Override
    public void readFromNbt(NbtCompound tag, Provider registryLookup)
    {
        flagCaptureCount.read(tag);
        markedKills.read(tag);
        baseLocation.read(tag);
        hasEverstar.read(tag);
        nextRaidStart.read(tag);
        previousRaidStart.read(tag);
        tick.read(tag);
    }

    @Override
    public void writeToNbt(NbtCompound tag, Provider registryLookup)
    {
        flagCaptureCount.write(tag);
        markedKills.write(tag);
        baseLocation.write(tag);
        hasEverstar.write(tag);
        nextRaidStart.write(tag);
        previousRaidStart.write(tag);
        tick.write(tag);
    }

    public static void test(ServerPlayerEntity thisEntity, GamePlayerData gamePlayerData, TeamRefrence currentZone)
    {
        if (!gamePlayerData.isVoicechatInitialized())
        {
            if (currentZone.hasMember(thisEntity))
            {
                if (VoicehcatHelper.joinTeamGroup(thisEntity))
                {
                    gamePlayerData.setVoicechatInitialized(true);
                }
            }
            else
            {
                if (VoicehcatHelper.leaveTeamGroup(thisEntity))
                {
                    gamePlayerData.setVoicechatInitialized(true);
                }
            }
        }
    }

    @Override
    public void serverTick()
    {
        if (tick.get() == 0)
        {
            setNextRaidTime();
        }

        if (tick.get() >= nextRaidStart.get())
        {
            startRaid();
        }

        tick.set(tick.get() + 1);
    }

    private void startRaid()
    {
        // set the start time
        long currentTick = tick.get();
        previousRaidStart.set(currentTick);

        TeamRefrence teamRefrence = TeamRefrence.of(this.team.getName());
        teamRefrence.sendMessageToMembers(server, Text.literal("A raid has begun!"), true);
        teamRefrence.sendMessageToMembers(server, Text.literal("A raid has begun! Time remaining: 20 min").formatted(Formatting.YELLOW), false);


        List<ServerPlayerEntity> list = server.getPlayerManager().getPlayerList().stream().filter((player) -> {
            return teamRefrence.hasMember(player);
        }).toList();
        if (!list.isEmpty())
        {
            for (ServerPlayerEntity player : list)
            {
                player.addStatusEffect(new StatusEffectInstance(getRandomStatusEffectForRaid(), RAID_DURATION, 0, false, true, true));
                player.networkHandler.send(new SoundPlayS2CPacket(SoundEvents.EVENT_RAID_HORN, SoundCategory.MASTER, player.getX(), player.getY(), player.getZ(), 100, 1, 1));
            }
        }

        // setup the next raid
        setNextRaidTime();
    }

    private Holder<StatusEffect> getRandomStatusEffectForRaid()
    {
        int rnd = new Random().nextInt(RAID_EFFECTS.length);
        return RAID_EFFECTS[rnd];
    }

    public void setNextRaidTime()
    {
        // (2 * 60 * 20 * 3)
        long nextRaid = tick.get() + server.getOverworld().getRandom().range(RAID_DURATION + (RAID_DURATION * 2), RAID_DURATION + (20 * 60 * 20 * 3));
        nextRaidStart.set(nextRaid);
    }

    public boolean isOnRaid()
    {
        return (tick.get() > previousRaidStart.get() && tick.get() < previousRaidStart.get() + RAID_DURATION);
    }
}
