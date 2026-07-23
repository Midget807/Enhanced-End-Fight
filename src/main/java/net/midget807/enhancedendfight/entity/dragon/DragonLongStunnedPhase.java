package net.midget807.enhancedendfight.entity.dragon;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DragonLongStunnedPhase extends AbstractDragonPhaseInstance {
    public static final int STUNNED_DURATION = 400;
    private static final TargetingConditions CHARGE_TARGETING = TargetingConditions.forCombat().range(150.0);
    @Nullable
    private Path currentPath;
    @Nullable
    private Vec3 targetLocation;
    private final TargetingConditions scanTargeting;
    private int stunnedTime;

    public DragonLongStunnedPhase(EnderDragon dragon) {
        super(dragon);
        this.scanTargeting = TargetingConditions.forCombat().range(20.0).selector(p_352809_ -> Math.abs(p_352809_.getY() - dragon.getY()) <= 10.0);
    }

    @Override
    public void doClientTick() {
        Vec3 rotVec = this.dragon.getHeadLookVector(1.0f).normalize();
        if (this.dragon.getXRot() != 150.0f) {
            this.dragon.setXRot(150.0f);
            this.dragon.head.setXRot(150.0f);
        }
    }

    @Override
    public void doServerTick() {
        stunnedTime++;
        if (this.currentPath != null) {
            this.currentPath = null;
        }
        BlockPos blockPos = this.dragon.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.dragon.blockPosition());
        if (blockPos.getY() <= 20) {
            blockPos = blockPos.atY(20);
        }
        if (this.targetLocation == null) {
            this.targetLocation = Vec3.atBottomCenterOf(
                    blockPos
            );
        }
        if (this.dragon.getHealth() <= 10.0f && this.closeToTargetPos()) {
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.TENACITY);
        }
        if (this.stunnedTime >= STUNNED_DURATION) {
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
    }

    private boolean closeToTargetPos() {
        if (this.targetLocation == null) return false;
        return this.dragon.position().distanceToSqr(this.targetLocation) <= 12;
    }

    @Override
    public void begin() {
        this.stunnedTime = 0;
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Override
    public float getFlySpeed() {
        return 7.0f;
    }

    @Override
    public float getTurnSpeed() {
        return 0.0f;
    }

    @Override
    public @Nullable Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return ModEnderDragonPhases.LONG_STUNNED;
    }
}
