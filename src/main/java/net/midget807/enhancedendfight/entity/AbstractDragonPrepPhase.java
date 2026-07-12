package net.midget807.enhancedendfight.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;

public abstract class AbstractDragonPrepPhase extends AbstractDragonPhaseInstance {
    public AbstractDragonPrepPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public boolean isSitting() {
        return true;
    }

    @Override
    public float onHurt(DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypes.GENERIC_KILL)) {
            return super.onHurt(damageSource, amount);
        }
        return 0.0f;
    }
}
