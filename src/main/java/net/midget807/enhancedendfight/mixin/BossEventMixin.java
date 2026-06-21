package net.midget807.enhancedendfight.mixin;

import net.midget807.enhancedendfight.util.injector.InvertedBar;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BossEvent.class)
public abstract class BossEventMixin implements InvertedBar {
    @Unique
    private boolean inverted = false;

    @Override
    public boolean isInverted() {
        return inverted;
    }

    @Override
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
}
