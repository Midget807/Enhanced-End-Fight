package net.midget807.enhancedendfight.item;

import net.midget807.enhancedendfight.entity.dragon.DragonChargePlayerCustomPhase;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
            if (dragon.getPhaseManager().getPhase(dragonPhase) == ModEnderDragonPhases.CHARGE_PLAYER) {
                ((DragonChargePlayerCustomPhase) dragon.getPhaseManager().getPhase(dragonPhase)).setTarget(new Vec3(player.getX(), player.getY(), player.getZ()));
            }
            dragon.getPhaseManager().setPhase(dragonPhase);
        });
        return super.use(level, player, usedHand);
    }
}
