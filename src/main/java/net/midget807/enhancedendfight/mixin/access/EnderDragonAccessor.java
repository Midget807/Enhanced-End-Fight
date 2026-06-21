package net.midget807.enhancedendfight.mixin.access;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.pathfinder.Node;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderDragon.class)
public interface EnderDragonAccessor {
    @Accessor("nodes")
    Node[] getNodes();

    @Accessor("nodeAdjacency")
    int[] getNodeAdjacency();
}
