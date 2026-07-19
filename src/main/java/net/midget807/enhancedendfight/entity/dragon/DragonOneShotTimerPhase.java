package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.midget807.enhancedendfight.util.injector.OneShotPhaseCrystals;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.jetbrains.annotations.Nullable;

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
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_TIMER;
    }
}
