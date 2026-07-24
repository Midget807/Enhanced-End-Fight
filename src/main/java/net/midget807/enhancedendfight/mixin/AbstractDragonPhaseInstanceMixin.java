package net.midget807.enhancedendfight.mixin;

import net.midget807.enhancedendfight.entity.dragon.NoMeleeDamage;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractDragonPhaseInstance.class)
public abstract class AbstractDragonPhaseInstanceMixin implements NoMeleeDamage {
    @Override
    public boolean shouldCancelMeleeDamage() {
        return false;
    }
}
