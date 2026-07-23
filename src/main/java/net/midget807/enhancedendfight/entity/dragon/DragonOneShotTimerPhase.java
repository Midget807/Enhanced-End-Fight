package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.entity.OneShotPhaseCrystal;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.midget807.enhancedendfight.util.injector.OneShotPhaseCrystals;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DragonOneShotTimerPhase extends AbstractDragonOneShotPhase{
    public static final int ONE_SHOT_PHASE_DURATION = 800;
    private int timer;
    private int tickDelta;
    @Nullable
    private Player targetToKill;
    private final TargetingConditions scanTargeting;
    private final ServerBossEvent oneShotEvent = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

    public DragonOneShotTimerPhase(EnderDragon dragon) {
        super(dragon);
        this.scanTargeting = TargetingConditions.forCombat().range(400.0);
    }

    @Override
    public void doServerTick() {
        timer++;
        tickDelta++;
        this.oneShotEvent.setProgress((float) this.timer / ONE_SHOT_PHASE_DURATION);
        List<ServerPlayer> alreadyAddedToEvent = this.oneShotEvent.getPlayers().stream().toList();
        for (Player player : this.dragon.level().players()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            if (!alreadyAddedToEvent.contains(serverPlayer) && serverPlayer.distanceToSqr(this.dragon) <= 22500) {
                this.oneShotEvent.addPlayer(serverPlayer);
            }
            if (alreadyAddedToEvent.contains(serverPlayer) && serverPlayer.distanceToSqr(this.dragon) > 22500) {
                this.oneShotEvent.removePlayer(serverPlayer);
            }
        }
        targetToKill = this.dragon
                .level()
                .getNearestPlayer(this.scanTargeting, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (this.targetToKill == null || !this.dragon.level().players().contains(this.targetToKill)) {
            this.rescanForTarget();
            System.out.println("rescanning");
        }
        if (targetToKill != null) {
            this.targetToKill.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0, false, false, false));
        }

        if (this.timer >= 20 && this.dragon.getDragonFight() != null && ((OneShotPhaseCrystals) this.dragon.getDragonFight()).getOneShotPhaseCrystals().isEmpty()) {
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.LONG_STUNNED);
            if (this.dragon.getDragonFight() != null) {
                ((OneShotPhaseCrystals) this.dragon.getDragonFight()).clearOneShotPhaseCrystals();
            }
            this.oneShotEvent.removeAllPlayers();
            this.timer = 0;
        }
        if (timer >= ONE_SHOT_PHASE_DURATION) {
            if (this.dragon.getDragonFight() != null) {
                ((OneShotPhaseCrystals) this.dragon.getDragonFight()).clearOneShotPhaseCrystals();
            }
            this.oneShotEvent.removeAllPlayers();
            this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.ONE_SHOT_KILL).setTargetToKill(targetToKill);
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_KILL);
        }
        if (tickDelta == 10) {
            if (this.targetToKill != null) {
                this.targetToKill.displayClientMessage(Component.literal("you are going to die").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED), true);
            }
        }
        if (tickDelta % 20 == 0) {
            tickDelta = 0;
        }
        if (targetToKill == null) {
            System.out.println("targetToKill is null");
        } else  {
            System.out.println("targetToKill: " + targetToKill.getName());
        }
    }

    private void rescanForTarget() {
        targetToKill = this.dragon
                .level()
                .getNearestPlayer(this.scanTargeting, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
    }

    @Override
    public void begin() {
        this.timer = 0;
        EndDragonFight fight = this.dragon.getDragonFight();
        if (fight != null) {
            if (!this.dragon.level().isClientSide) {
                List<SpikeFeature.EndSpike> spikes = SpikeFeature.getSpikesForLevel((ServerLevel) this.dragon.level());
                List<BlockPos> towerCrystalPositions = new ArrayList<>();
                Map<Integer, BlockPos> oneShotCrystalPositions = new HashMap<>();
                for (SpikeFeature.EndSpike spike : spikes) {
                    BlockPos pos = new BlockPos(spike.getCenterX(), spike.getHeight(), spike.getCenterZ());
                    towerCrystalPositions.add(pos);
                }
                List<Integer> allowedPositions = new ArrayList<>();
                for (int i = 0; i < towerCrystalPositions.size(); i++) {
                    allowedPositions.add(i);
                }

                for (int i = 0; i < 4; i++) {
                    int random = allowedPositions.get(this.dragon.level().random.nextInt(allowedPositions.size()));

                    BlockPos pos = towerCrystalPositions.get(random);
                    oneShotCrystalPositions.put(random, pos);
                    allowedPositions.remove((Object) (random));


                    if (allowedPositions.contains(random + 1)) {
                        allowedPositions.remove((Object) (random + 1));
                    } else if (allowedPositions.contains(random - 1)) {
                        allowedPositions.remove((Object) (random - 1));
                    }
                }
                BlockPos beamPos = this.dragon.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(new BlockPos(
                        this.dragon.getFightOrigin().getX(),
                        this.dragon.getFightOrigin().getY(),
                        this.dragon.getFightOrigin().getZ()
                )));
                beamPos = beamPos.above(50);
                for (Map.Entry<Integer, BlockPos> entry : oneShotCrystalPositions.entrySet()) {
                    List<EndCrystal> crystalPresentAtTower = this.dragon.level().getEntitiesOfClass(EndCrystal.class, new AABB(entry.getValue()).inflate(1.0));
                    if (!crystalPresentAtTower.isEmpty()) {
                        for (EndCrystal crystal : crystalPresentAtTower) {
                            crystal.discard();
                        }
                        OneShotPhaseCrystal crystal = new OneShotPhaseCrystal(this.dragon.level(), entry.getValue());
                        crystal.setShowBottom(true);
                        crystal.setShouldReplaceOnDeath(true);
                        crystal.setBeamTarget(beamPos);
                        this.dragon.level().addFreshEntity(crystal);
                    } else {
                        OneShotPhaseCrystal crystal = new OneShotPhaseCrystal(this.dragon.level(), entry.getValue());
                        crystal.setShowBottom(true);
                        crystal.setShouldReplaceOnDeath(false);
                        crystal.setBeamTarget(beamPos);
                        this.dragon.level().addFreshEntity(crystal);
                    }
                }
            }

            ((OneShotPhaseCrystals) fight).updateOneShotPhaseCrystals();
        }
    }

    @Override
    public void onCrystalDestroyed(EndCrystal crystal, BlockPos pos, DamageSource dmgSrc, @Nullable Player plyr) {
        EndDragonFight fight = this.dragon.getDragonFight();
        if (fight != null) {
            ((OneShotPhaseCrystals) fight).updateOneShotPhaseCrystals();
        }
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_TIMER;
    }
}
