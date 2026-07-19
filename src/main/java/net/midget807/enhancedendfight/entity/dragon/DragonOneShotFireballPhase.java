package net.midget807.enhancedendfight.entity.dragon;

import com.mojang.logging.LogUtils;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import org.slf4j.Logger;

import javax.annotation.Nullable;

public class DragonOneShotFireballPhase extends AbstractDragonOneShotPhase{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int FIREBALL_CHARGE_AMOUNT = 5;
    private int fireballCharge;
    @Nullable
    private LivingEntity attackTarget;

    public DragonOneShotFireballPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doClientTick() {

    }

    @Override
    public void doServerTick() {
        super.doServerTick();
        if (this.attackTarget == null) {
            LOGGER.warn("Skipping player strafe phase because no player was found");
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_SCAN);
        } else {
            if (this.attackTarget.distanceToSqr(this.dragon) < 22500.0) {
                if (this.dragon.hasLineOfSight(this.attackTarget)) {
                    this.fireballCharge++;

                }
            }
        }
    }

    @Override
    public void begin() {

    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_FIREBALL;
    }
}
