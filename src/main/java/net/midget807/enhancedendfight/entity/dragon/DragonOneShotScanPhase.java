package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;

public class DragonOneShotScanPhase extends AbstractDragonPrepPhase{
    private static final int SCANNING_TICKS = 50;
    private static final int ATTACK_Y_VIEW_RANGE = 300;
    private static final int ATTACK_VIEW_RANGE = 300;
    private static final int CHARGE_VIEW_RANGE = 150;
    private static final TargetingConditions CHARGE_TARGETING = TargetingConditions.forCombat().range(150.0);
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
        LivingEntity livingEntity = this.dragon
                .level()
                .getNearestPlayer(this.scanTargeting, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (livingEntity != null) {
            if (this.scanningTime > SCANNING_TICKS) {
                this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_ATTACK);
            }
        }
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_SCAN;
    }
}
