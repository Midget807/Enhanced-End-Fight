package net.midget807.enhancedendfight.entity.dragon;

import com.mojang.logging.LogUtils;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonChargePlayerPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.Nullable;

public class DragonChargePlayerCustomPhase extends AbstractDragonPhaseInstance {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int CHARGE_TRY_TIME = 400;
    @Nullable
    private Vec3 targetLocation;
    private int timeSinceCharge;
    private int tryTime;

    public DragonChargePlayerCustomPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doServerTick() {
        tryTime++;
        if (this.tryTime >= CHARGE_TRY_TIME) {
            LOGGER.warn("Aborting charge player as no target was set.");
            this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
        }
        Player player = this.dragon.level().getNearestPlayer(TargetingConditions.forCombat().range(400.0).ignoreLineOfSight(), this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (player != null) {
            this.targetLocation = player.position();
        }
        double distance = this.targetLocation.distanceToSqr(this.dragon.position());
        if (this.timeSinceCharge > 0 && this.timeSinceCharge++ >= 40 + distance) {
            targetLocation = null;
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.STUNNED);
        } else {
            double d0 = this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
            if (d0 < 100.0 || d0 > 22500.0 || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
                this.timeSinceCharge++;
            }
        }
    }

    @Override
    public void begin() {
        this.timeSinceCharge = 0;
        this.tryTime = 0;
    }

    public void setTarget(Vec3 targetLocation) {
        this.targetLocation = targetLocation;
    }

    @Override
    public float getFlySpeed() {
        return 3.0F;
    }

    @Override
    public float getTurnSpeed() {
        float f = (float)this.dragon.getDeltaMovement().horizontalDistance() + 1.0F;
        float f1 = Math.min(f, 40.0F);
        return f1 / f;
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonChargePlayerCustomPhase> getPhase() {
        return ModEnderDragonPhases.CHARGE_PLAYER;
    }
}
