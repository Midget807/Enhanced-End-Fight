package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.phys.Vec3;

public class DragonLongStunnedSittingPhase extends AbstractDragonPhaseInstance implements NoMeleeDamage {
    public int stunnedDuration = 500;
    private static final TargetingConditions CHARGE_TARGETING = TargetingConditions.forCombat().range(150.0);
    private final TargetingConditions scanTargeting;
    private int stunnedTime;
    private int tickDelta;

    public DragonLongStunnedSittingPhase(EnderDragon dragon) {
        super(dragon);
        this.scanTargeting = TargetingConditions.forCombat().range(20.0).selector(p_352809_ -> Math.abs(p_352809_.getY() - dragon.getY()) <= 10.0);
    }

    @Override
    public void doServerTick() {
        stunnedTime++;
        if (this.dragon.getHealth() <= 0 && this.dragon.getPhaseManager().getCurrentPhase() != EnderDragonPhase.DYING) {
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.TENACITY);
        }
        if (this.dragon.getMaxHealth() <= 2.0) {

        }
        if (this.stunnedTime >= stunnedDuration) {
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

    public void setStunnedDuration(int stunnedDuration) {
        this.stunnedTime = stunnedDuration;
    }

    public int getStunnedDuration() {
        return this.stunnedDuration;
    }

    @Override
    public EnderDragonPhase<DragonLongStunnedSittingPhase> getPhase() {
        return ModEnderDragonPhases.LONG_STUNNED_SITTING;
    }

    @Override
    public boolean shouldCancelMeleeDamage() {
        return true;
    }
}
