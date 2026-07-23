package net.midget807.enhancedendfight.registry;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.Nullable;

public class ModDamageTypes {
    public static DamageSource oneShot(LivingEntity target, @Nullable LivingEntity attacker) {
        return target.damageSources().source(ONE_SHOT, attacker);
    }
    public static DamageSource oneShot(LivingEntity target) {
        return target.damageSources().source(ONE_SHOT);
    }

    public static DamageSource dragonMagic(LivingEntity target, @Nullable LivingEntity attacker) {
        return target.damageSources().source(DRAGON_MAGIC, attacker);
    }
    public static DamageSource dragonMagic(LivingEntity target) {
        return target.damageSources().source(DRAGON_MAGIC);
    }

    public static final ResourceKey<DamageType> ONE_SHOT = create("one_shot");
    public static final ResourceKey<DamageType> DRAGON_MAGIC = create("dragon_magic");

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, EnhancedEndFightMain.id(name));
    }

    public static class Tags {
        public static final TagKey<DamageType> BYPASSES_TOTEM = create("bypasses_totem");
        public static final TagKey<DamageType> RANGED = create("ranged");
        public static final TagKey<DamageType> MELEE = create("melee");
        public static final TagKey<DamageType> MAGIC = create("magic");

        private static TagKey<DamageType> create(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, EnhancedEndFightMain.id(name));
        }
    }
}
