package net.midget807.enhancedendfight.entity;

import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DragonOneShotPhase extends AbstractDragonPhaseInstance {
    @Nullable
    private Vec3 targetLocation;

    public DragonOneShotPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doClientTick() {
        Vec3 vec3 = this.dragon.getHeadLookVector(1.0F).normalize();
        vec3.yRot((float) (-Math.PI / 4));
        double d0 = this.dragon.head.getX();
        double d1 = this.dragon.head.getY(0.5);
        double d2 = this.dragon.head.getZ();

        for (int i = 0; i < 8; i++) {
            RandomSource randomsource = this.dragon.getRandom();
            double d3 = d0 + randomsource.nextGaussian() / 2.0;
            double d4 = d1 + randomsource.nextGaussian() / 2.0;
            double d5 = d2 + randomsource.nextGaussian() / 2.0;
            Vec3 vec31 = this.dragon.getDeltaMovement();
            this.dragon
                    .level()
                    .addParticle(ParticleTypes.DRAGON_BREATH, d3, d4, d5, -vec3.x * 0.08F + vec31.x, -vec3.y * 0.3F + vec31.y, -vec3.z * 0.08F + vec31.z);
            vec3.yRot((float) (Math.PI / 16));
        }
    }

    @Override
    public void doServerTick() {
        if (this.targetLocation == null) {
            BlockPos blockPos = this.dragon.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(new BlockPos(
                    this.dragon.getFightOrigin().getX(),
                    this.dragon.getFightOrigin().getY(),
                    this.dragon.getFightOrigin().getZ()
            )));
            blockPos = blockPos.above(50);
            this.targetLocation = Vec3.atBottomCenterOf(
                    blockPos
            );
        }

        if (this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ()) < 1.0) {
            this.dragon.getPhaseManager().getPhase(EnderDragonPhase.SITTING_FLAMING).resetFlameCount();
            this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
        }
    }


    @Override
    public void begin() {
        this.targetLocation = null;
    }

    @Override
    public float getFlySpeed() {
        return 1.5f;
    }

    @Override
    public float getTurnSpeed() {
        float f = (float)this.dragon.getDeltaMovement().horizontalDistance() + 1.0F;
        float f1 = Math.min(f, 40.0F);
        return f1 / f;
    }

    @Override
    public @Nullable Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonOneShotPhase> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT;
    }
}
