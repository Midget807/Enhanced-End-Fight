package net.midget807.enhancedendfight.effect;

import net.midget807.enhancedendfight.registry.ModDamageTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class DragonMagicEffect extends MobEffect {
    public DragonMagicEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public DragonMagicEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return livingEntity.hurt(ModDamageTypes.dragonMagic(livingEntity),  4 << amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}
