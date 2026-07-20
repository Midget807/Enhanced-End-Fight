package net.midget807.enhancedendfight.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class PhaseDebuggerItem extends SuccessItem {
    private final EnderDragonPhase<?> dragonPhase;

    public PhaseDebuggerItem(Properties properties, EnderDragonPhase<?> dragonPhase) {
        super(properties);
        this.dragonPhase = dragonPhase;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        List<EnderDragon> dragons = level.getEntitiesOfClass(EnderDragon.class, player.getBoundingBox().inflate(500));
        dragons.forEach(dragon -> {
            dragon.getPhaseManager().setPhase(dragonPhase);
        });
        return super.use(level, player, usedHand);
    }
}
