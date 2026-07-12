package net.midget807.enhancedendfight.entity;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;

public class DragonOneShotAttackPhase extends AbstractDragonPhaseInstance {
    public DragonOneShotAttackPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doServerTick() {

    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_ATTACK;
    }
}
