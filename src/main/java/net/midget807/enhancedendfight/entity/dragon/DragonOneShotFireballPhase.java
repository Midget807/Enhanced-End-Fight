package net.midget807.enhancedendfight.entity.dragon;

import com.mojang.logging.LogUtils;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public class DragonOneShotFireballPhase extends AbstractDragonOneShotPhase{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int FIREBALL_CHARGE_AMOUNT = 5;
    private int fireballCharge;
    @Nullable
    private List<? extends LivingEntity> attackTargets;

    public DragonOneShotFireballPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doClientTick() {

    }

    @Override
    public void doServerTick() {
        super.doServerTick();
        if (attackTargets == null || attackTargets.isEmpty()) {
            LOGGER.warn("Skipping player fireball phase because no player was found");
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_SCAN);
        } else {
            for (LivingEntity attackTarget : attackTargets) {
                Vec3 vec32 = new Vec3(attackTarget.getX() - this.dragon.getX(), 0.0, attackTarget.getZ() - this.dragon.getZ()).normalize();
                Vec3 vec3 = new Vec3(
                        (double) Mth.sin(this.dragon.getYRot() * (float) (Math.PI / 180.0)),
                        0.0,
                        (double) (-Mth.cos(this.dragon.getYRot() * (float) (Math.PI / 180.0)))
                )
                        .normalize();
                float f1 = (float) vec3.dot(vec32);
                float f = (float) (Math.acos((double) f1) * 180.0F / (float) Math.PI);
                f += 0.5F;
                double d14 = 1.0;
                Vec3 vec33 = this.dragon.getViewVector(0.0F);
                double d6 = this.dragon.head.getX() - vec33.x * 1.0;
                double d7 = this.dragon.head.getY(0.5) - 1.5;
                double d8 = this.dragon.head.getZ() - vec33.z * 1.0;
                double d9 = attackTarget.getX() - d6;
                double d10 = attackTarget.getY(0.5) - d7;
                double d11 = attackTarget.getZ() - d8;
                Vec3 vec31 = new Vec3(d9, d10, d11);
                if (!this.dragon.isSilent()) {
                    this.dragon.level().levelEvent(null, 1017, this.dragon.blockPosition(), 0);
                }

                DragonFireball dragonfireball = new DragonFireball(this.dragon.level(), this.dragon, vec31.normalize());
                dragonfireball.moveTo(d6, d7, d8, 0.0F, 0.0F);
                this.dragon.level().addFreshEntity(dragonfireball);
            }
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_SCAN);
        }
    }

    @Override
    public void begin() {
        this.fireballCharge = 0;
    }

    public void setAttackTarget(List<? extends LivingEntity> target) {
        attackTargets = target;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_FIREBALL;
    }
}
