package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;

public abstract class AbstractDragonOneShotPhase extends AbstractDragonPhaseInstance {

    public AbstractDragonOneShotPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public boolean isSitting() {
        return true;
    }

    @Override
    public void doServerTick() {
        this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.ONE_SHOT_TIMER).doServerTick();
    }

    @Override
    public float onHurt(DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.onHurt(damageSource, amount);
        } else {
            return 0.0f;
        }
    }
}
