package net.midget807.enhancedendfight.item;

import net.midget807.enhancedendfight.event.BlockPosHighlightRenderer;
import net.midget807.enhancedendfight.mixin.access.EnderDragonAccessor;
import net.midget807.enhancedendfight.util.BlockPosHighlights;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class NodeMarkerItem extends Item {
    public NodeMarkerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        List<EnderDragon> dragons = level.getEntitiesOfClass(EnderDragon.class, player.getBoundingBox().inflate(500));
        dragons.forEach(dragon -> {
            BlockPosHighlights.clear();
            Arrays.stream(((EnderDragonAccessor) dragon).getNodes()).toList().forEach(node -> {
                if (node != null) {
                    BlockPosHighlights.add(node.asBlockPos());
                }
            });
        });
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
