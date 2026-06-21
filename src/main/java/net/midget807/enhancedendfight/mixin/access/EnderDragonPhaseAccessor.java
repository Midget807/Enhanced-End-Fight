package net.midget807.enhancedendfight.mixin.access;

import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EnderDragonPhase.class)
public interface EnderDragonPhaseAccessor {
    @Accessor("phases")
    EnderDragonPhase<?>[] getPhases();

    @Invoker("<init>")
    static <T extends DragonPhaseInstance> EnderDragonPhase<T> invokeNew(
            int id,
            Class<T> instanceClass,
            String name
    ) {
        throw new AssertionError();
    }

    @Invoker("create")
    static <T extends DragonPhaseInstance> EnderDragonPhase<T> invokeCreate(
            Class<T> phaseClass,
            String name
    ) {
        throw new AssertionError();
    }
}
