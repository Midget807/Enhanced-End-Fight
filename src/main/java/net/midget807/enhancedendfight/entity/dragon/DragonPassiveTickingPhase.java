package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;

public class DragonPassiveTickingPhase extends AbstractDragonPhaseInstance {
    private boolean isInit = false;
    private boolean startDamageResistance;

    public DragonPassiveTickingPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doServerTick() {
        if (this.startDamageResistance) {
            this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.DAMAGE_RESISTANCE).begin();
            this.dragon.getPhaseManager().getPhase(ModEnderDragonPhases.DAMAGE_RESISTANCE).doServerTick();
        }
    }

    @Override
    public void begin() {
        isInit = true;
        this.startDamageResistance = false;
    }

    public void setStartDamageResistance(boolean startDamageResistance) {
        this.startDamageResistance = startDamageResistance;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.PASSIVE_TICKING;
    }
}
