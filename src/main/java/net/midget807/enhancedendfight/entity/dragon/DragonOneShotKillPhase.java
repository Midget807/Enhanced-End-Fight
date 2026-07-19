package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModDamageTypes;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class DragonOneShotKillPhase extends AbstractDragonOneShotPhase{
    public static final int PARTICLE_TIME_SYNC = 10;
    @Nullable
    private Player targetToKill;
    private int particleTimeSync;

    public DragonOneShotKillPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doClientTick() {
        if (this.targetToKill != null) {
            //todo summon kill particle
        }
    }

    @Override
    public void doServerTick() {
        this.particleTimeSync++;
        if (this.particleTimeSync > PARTICLE_TIME_SYNC || targetToKill == null) {
            this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
        }
        if (this.targetToKill != null && this.particleTimeSync == PARTICLE_TIME_SYNC) {
            targetToKill.hurt(ModDamageTypes.oneShot(this.targetToKill, this.dragon), Float.MAX_VALUE);
        }
    }

    @Override
    public void begin() {
        this.particleTimeSync = 0;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_KILL;
    }

    public void setTargetToKill(Player targetToKill) {
        this.targetToKill = targetToKill;
    }
}
