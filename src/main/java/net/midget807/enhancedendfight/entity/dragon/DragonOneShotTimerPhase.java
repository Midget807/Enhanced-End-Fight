package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.entity.OneShotPhaseCrystal;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.midget807.enhancedendfight.util.injector.OneShotPhaseCrystals;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.end.EndDragonFight;
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
    @Nullable
    private Player targetToKill;
    private final TargetingConditions scanTargeting;

    public DragonOneShotTimerPhase(EnderDragon dragon) {
        super(dragon);
        this.scanTargeting = TargetingConditions.forCombat().range(300.0);
    }

    @Override
    public void doServerTick() {
        timer++;
        targetToKill = this.dragon
                .level()
                .getNearestPlayer(this.scanTargeting, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (this.targetToKill == null || !this.dragon.level().players().contains(this.targetToKill)) {
            this.rescanForTarget();
        }
        if (this.dragon.getDragonFight() != null && ((OneShotPhaseCrystals) this.dragon.getDragonFight()).getOneShotCrystalsAlive() <= 0) {
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.STUNNED);
        }
        if (timer >= ONE_SHOT_PHASE_DURATION) {
            this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.ONE_SHOT_KILL).setTargetToKill(targetToKill);
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_KILL);
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
                for (Map.Entry<Integer, BlockPos> entry : oneShotCrystalPositions.entrySet()) {
                    List<EndCrystal> crystalPresentAtTower = this.dragon.level().getEntitiesOfClass(EndCrystal.class, new AABB(entry.getValue()));
                    if (!crystalPresentAtTower.isEmpty()) {
                        for (EndCrystal crystal : crystalPresentAtTower) {
                            crystal.discard();
                        }
                        OneShotPhaseCrystal crystal = new OneShotPhaseCrystal(this.dragon.level(), entry.getValue());
                        crystal.setShowBottom(true);
                        crystal.setShouldReplaceOnDeath(true);
                        this.dragon.level().addFreshEntity(crystal);
                    } else {
                        OneShotPhaseCrystal crystal = new OneShotPhaseCrystal(this.dragon.level(), entry.getValue());
                        crystal.setShowBottom(true);
                        this.dragon.level().addFreshEntity(crystal);
                    }
                }
            }
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
