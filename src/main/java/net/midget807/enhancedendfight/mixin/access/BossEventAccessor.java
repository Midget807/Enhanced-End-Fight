package net.midget807.enhancedendfight.mixin.access;

import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(BossEvent.class)
public interface BossEventAccessor {
    @Accessor("id")
    UUID getUuid();
}
