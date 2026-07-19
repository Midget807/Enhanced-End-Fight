package net.midget807.enhancedendfight.entity.dragon;

import com.mojang.logging.LogUtils;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class DragonOneShotApproachPhase extends AbstractDragonPhaseInstance {
    private static final TargetingConditions NEAR_EGG_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int PHASE_RECOVERY_TIME = 10;
    @Nullable
    private Path currentPath;
    @Nullable
    private Vec3 targetLocation;

    public DragonOneShotApproachPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void doServerTick() {
        double distanceToTarget = this.targetLocation == null ? 0.0 : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        // var < 10 || ver > 150 blocks
        if (distanceToTarget < 100.0 || distanceToTarget > 22500.0) {
            this.findNewTarget();
        }
    }

    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isDone()) {
            int i = this.dragon.findClosestNode();
            BlockPos blockpos = this.dragon
                    .level()
                    .getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(new BlockPos(
                            this.dragon.getFightOrigin().getX(),
                            this.dragon.getFightOrigin().getY(),
                            this.dragon.getFightOrigin().getZ()
                    )));
            blockpos = blockpos.above(50);
            Player player = this.dragon
                    .level()
                    .getNearestPlayer(NEAR_EGG_TARGETING, this.dragon, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
            int j;
            if (player != null) {
                Vec3 vec3 = new Vec3(player.getX(), 0.0, player.getZ()).normalize();
                j = this.dragon.findClosestNode(-vec3.x * 40.0, 105.0, -vec3.z * 40.0);
            } else {
                j = this.dragon.findClosestNode(40.0, (double)blockpos.getY(), 0.0);
            }

            Node node = new Node(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.currentPath = this.dragon.findPath(i, j, node);
            if (this.currentPath != null) {
                this.currentPath.advance();
            }
        }

        this.navigateToNextPathNode();
        if (this.currentPath != null && this.currentPath.isDone()) {
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT);
        }
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isDone()) {
            Vec3i vec3i = this.currentPath.getNextNodePos();
            this.currentPath.advance();
            double d0 = (double)vec3i.getX();
            double d1 = (double)vec3i.getZ();

            double d2;
            do {
                d2 = (double)((float)vec3i.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
            } while (d2 < (double)vec3i.getY());

            this.targetLocation = new Vec3(d0, d2, d1);
        }
    }

    @Override
    public void begin() {
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Override
    public float getFlySpeed() {
        return 3.0f;
    }

    @Override
    public @Nullable Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonOneShotApproachPhase> getPhase() {
        return ModEnderDragonPhases.ONE_SHOT_APPROACH;
    }
}
