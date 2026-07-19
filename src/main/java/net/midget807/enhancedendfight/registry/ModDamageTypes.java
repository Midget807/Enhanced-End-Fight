package net.midget807.enhancedendfight.registry;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class ModDamageTypes {
    public static DamageSource oneShot(LivingEntity target, @Nullable LivingEntity attacker) {
        return target.damageSources().source(ONE_SHOT, attacker);
    }
    public static DamageSource oneShot(LivingEntity target) {
        return target.damageSources().source(ONE_SHOT);
    }

    public static final ResourceKey<DamageType> ONE_SHOT = register("one_shot");

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, EnhancedEndFightMain.id(name));
    }
}
