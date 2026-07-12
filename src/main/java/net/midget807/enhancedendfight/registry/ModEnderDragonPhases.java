package net.midget807.enhancedendfight.registry;

import net.midget807.enhancedendfight.entity.DragonOneShotApproachPhase;
import net.midget807.enhancedendfight.entity.DragonOneShotAttackPhase;
import net.midget807.enhancedendfight.entity.DragonOneShotPhase;
import net.midget807.enhancedendfight.entity.DragonOneShotScanPhase;
import net.midget807.enhancedendfight.entity.DragonStunnedPhase;
import net.midget807.enhancedendfight.entity.DragonTenacityPhase;
import net.midget807.enhancedendfight.mixin.access.EnderDragonPhaseAccessor;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.neoforged.bus.api.IEventBus;

public class ModEnderDragonPhases {
    //Completion: 9/10
    public static final EnderDragonPhase<DragonOneShotApproachPhase> ONE_SHOT_APPROACH = create(DragonOneShotApproachPhase.class, "OneShotApproach");
    //Completion: 5/10
    public static final EnderDragonPhase<DragonOneShotPhase> ONE_SHOT = create(DragonOneShotPhase.class, "OneShot");
    //Completion: 0/10
    public static final EnderDragonPhase<DragonOneShotScanPhase> ONE_SHOT_SCAN = create(DragonOneShotScanPhase.class, "OneShotScan");
    //Completion: 0/10
    public static final EnderDragonPhase<DragonOneShotAttackPhase> ONE_SHOT_ATTACK = create(DragonOneShotAttackPhase.class, "OneShotAttack");
    //Completion: 7/10
    public static final EnderDragonPhase<DragonStunnedPhase> STUNNED = create(DragonStunnedPhase.class, "Stunned");
    //Completion: 7/10
    public static final EnderDragonPhase<DragonTenacityPhase> TENACITY = create(DragonTenacityPhase.class, "Tenacity");

    public static <T extends DragonPhaseInstance> EnderDragonPhase<T> create(Class<T> phase, String name) {
        return EnderDragonPhaseAccessor.invokeCreate(phase, name);
    }

    public static void registerModEnderDragonPhases(IEventBus bus) {

    }
}
