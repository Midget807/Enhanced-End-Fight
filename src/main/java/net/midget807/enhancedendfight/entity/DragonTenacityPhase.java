package net.midget807.enhancedendfight.entity;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.phys.Vec3;

public class DragonTenacityPhase extends AbstractDragonPhaseInstance {
    public static final int STUNNED_DURATION = 100;
    private static final TargetingConditions CHARGE_TARGETING = TargetingConditions.forCombat().range(150.0);
    private final TargetingConditions scanTargeting;
    private int stunnedTime;
    private int tickDelta;

    public DragonTenacityPhase(EnderDragon dragon) {
        super(dragon);
        this.scanTargeting = TargetingConditions.forCombat().range(20.0).selector(p_352809_ -> Math.abs(p_352809_.getY() - dragon.getY()) <= 10.0);
    }

    @Override
    public void doServerTick() {
        stunnedTime++;
        this.tickDelta++;
        if (this.tickDelta % 10 == 0) {
            this.dragon.heal(1.0f);
        }
        if (this.stunnedTime >= STUNNED_DURATION) {
            LivingEntity livingentity = this.dragon.level().getNearestPlayer(CHARGE_TARGETING, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
            this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
            if (livingentity != null) {
                this.dragon.getPhaseManager().setPhase(EnderDragonPhase.CHARGING_PLAYER);
                this.dragon
                        .getPhaseManager()
                        .getPhase(EnderDragonPhase.CHARGING_PLAYER)
                        .setTarget(new Vec3(livingentity.getX(), livingentity.getY(), livingentity.getZ()));
            }
        }
        if (this.tickDelta >= 20) {
            this.tickDelta = 0;
        }
    }

    @Override
    public void begin() {
        this.stunnedTime = 0;
        this.tickDelta = 0;
    }

    @Override
    public EnderDragonPhase<DragonTenacityPhase> getPhase() {
        return ModEnderDragonPhases.TENACITY;
    }
}
