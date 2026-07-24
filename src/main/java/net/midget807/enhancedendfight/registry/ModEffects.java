package net.midget807.enhancedendfight.registry;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.effect.DragonMagicEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, EnhancedEndFightMain.MODID);

    public static final Holder<MobEffect> DRAGON_MAGIC = MOB_EFFECTS.register("dragon_magic",
            () -> new DragonMagicEffect(MobEffectCategory.HARMFUL, 0xb608d4)
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
