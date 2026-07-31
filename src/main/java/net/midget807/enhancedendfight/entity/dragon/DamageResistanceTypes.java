package net.midget807.enhancedendfight.entity.dragon;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum DamageResistanceTypes implements StringRepresentable {
    MELEE("melee"),
    RANGED("ranged"),
    MAGIC("magic"),
    ALL("all"),
    NONE("none");

    private final String name;
    DamageResistanceTypes(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
