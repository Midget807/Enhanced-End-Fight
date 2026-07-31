package net.midget807.enhancedendfight.entity.dragon;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;

public abstract class AbstractDragonPassiveTickPhase extends AbstractDragonPhaseInstance {
    public int timer;

    public AbstractDragonPassiveTickPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doServerTick() {
        timer++;
    }
}
