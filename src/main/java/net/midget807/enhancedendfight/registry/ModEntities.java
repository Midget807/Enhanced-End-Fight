package net.midget807.enhancedendfight.registry;

import net.midget807.enhancedendfight.EnhancedEndFightClient;
import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.entity.OneShotPhaseCrystal;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, EnhancedEndFightMain.MODID);

    public static final Supplier<EntityType<OneShotPhaseCrystal>> ONE_SHOT_CRYSTAL =
            ENTITY_TYPES.register("one_shot_crystal", () -> EntityType.Builder.of(OneShotPhaseCrystal::new, MobCategory.MISC)
                    .sized(2.0f, 2.0f).build("one_shot_crystal"));

    public static void register (IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
