package net.midget807.enhancedendfight.util.injector;

import net.midget807.enhancedendfight.entity.OneShotPhaseCrystal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

import java.util.List;
import java.util.Map;

public interface OneShotPhaseCrystals {
    List<OneShotPhaseCrystal> getOneShotPhaseCrystals();
    int getOneShotCrystalsAlive();

    Map<BlockPos, EndCrystal> getVanillaEndCrystals();

    void updateOneShotPhaseCrystals();
}
