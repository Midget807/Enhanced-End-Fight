package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class DragonOneShotScanPhase extends AbstractDragonOneShotPhase {
    private static final int SCANNING_TICKS = 80;
    private static final int ATTACK_Y_VIEW_RANGE = 600;
    private static final int ATTACK_VIEW_RANGE = 400;
    private static final int CHARGE_VIEW_RANGE = 150;
    private static final TargetingConditions CHARGE_TARGETING = TargetingConditions.forCombat().range(ATTACK_VIEW_RANGE);
    private final TargetingConditions scanTargeting;
    private int scanningTime;

    public DragonOneShotScanPhase(EnderDragon dragon) {
        super(dragon);
        this.scanTargeting = TargetingConditions.forCombat().range(ATTACK_VIEW_RANGE).selector(p_352809_ -> Math.abs(p_352809_.getY() - dragon.getY()) <= ATTACK_Y_VIEW_RANGE);
    }

    @Override
    public void doServerTick() {
        super.doServerTick();
        this.scanningTime++;
        if (this.scanningTime > SCANNING_TICKS) {

            List<Player> livingEntities = this.dragon
                    .level()
                    .getEntitiesOfClass(Player.class, this.dragon.getBoundingBox().inflate(ATTACK_VIEW_RANGE, ATTACK_Y_VIEW_RANGE, ATTACK_VIEW_RANGE), livingEntity1 -> !livingEntity1.isSpectator());
            if (!livingEntities.isEmpty()) {
                this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.ONE_SHOT_FIREBALL).setAttackTarget(livingEntities);
                this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_ATTACK);
            }
        }
    }

    @Override
    public void begin() {
        scanningTime = 0;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_SCAN;
    }
}
