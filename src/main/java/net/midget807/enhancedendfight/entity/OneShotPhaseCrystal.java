package net.midget807.enhancedendfight.entity;

import net.midget807.enhancedendfight.registry.ModEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level;

public class OneShotPhaseCrystal extends EndCrystal {
    public OneShotPhaseCrystal(EntityType<? extends EndCrystal> entityType, Level level) {
        super(entityType, level);
    }

}
