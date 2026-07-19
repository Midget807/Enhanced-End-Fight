package net.midget807.enhancedendfight.registry;

import net.midget807.enhancedendfight.entity.dragon.*;
import net.midget807.enhancedendfight.mixin.access.EnderDragonPhaseAccessor;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.neoforged.bus.api.IEventBus;

public class ModEnderDragonPhases {
    //Completion: 9/10
    public static final EnderDragonPhase<DragonOneShotApproachPhase> ONE_SHOT_APPROACH = create(DragonOneShotApproachPhase.class, "OneShotApproach");
    //Completion: 5/10
    public static final EnderDragonPhase<DragonOneShotLandingPhase> ONE_SHOT = create(DragonOneShotLandingPhase.class, "OneShot");
    //Completion: 0/10
    public static final EnderDragonPhase<DragonOneShotTimerPhase> ONE_SHOT_TIMER = create(DragonOneShotTimerPhase.class, "OneShotTimer");
    //Completion: 0/10
    public static final EnderDragonPhase<DragonOneShotScanPhase> ONE_SHOT_SCAN = create(DragonOneShotScanPhase.class, "OneShotScan");
    //Completion: 0/10
    public static final EnderDragonPhase<DragonOneShotAttackPhase> ONE_SHOT_ATTACK = create(DragonOneShotAttackPhase.class, "OneShotAttack");
    //Completion: 0/10
    public static final EnderDragonPhase<DragonOneShotFireballPhase> ONE_SHOT_FIREBALL = create(DragonOneShotFireballPhase.class, "OneShotFireball");
    //Completion: 9/10
    public static final EnderDragonPhase<DragonOneShotKillPhase> ONE_SHOT_KILL = create(DragonOneShotKillPhase.class, "OneShotKill");
    //Completion: 7/10
    public static final EnderDragonPhase<DragonStunnedPhase> STUNNED = create(DragonStunnedPhase.class, "Stunned");
    //Completion: 7/10
    public static final EnderDragonPhase<DragonLongStunnedPhase> LONG_STUNNED = create(DragonLongStunnedPhase.class, "LongStunned");
    //Completion: 7/10
    public static final EnderDragonPhase<DragonTenacityPhase> TENACITY = create(DragonTenacityPhase.class, "Tenacity");

    public static <T extends DragonPhaseInstance> EnderDragonPhase<T> create(Class<T> phase, String name) {
        return EnderDragonPhaseAccessor.invokeCreate(phase, name);
    }

    public static void registerModEnderDragonPhases(IEventBus bus) {

    }
}
