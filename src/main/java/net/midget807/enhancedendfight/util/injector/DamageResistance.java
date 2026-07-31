package net.midget807.enhancedendfight.util.injector;

import net.midget807.enhancedendfight.entity.dragon.DamageResistanceTypes;

public interface DamageResistance {
    DamageResistanceTypes getResistanceType();
    void setResistanceType(DamageResistanceTypes resistanceType);
}
