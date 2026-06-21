package net.midget807.enhancedendfight.item;

import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class PhaseQueryItem extends Item {
    public PhaseQueryItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide) {
            List<EnderDragon> dragons = level.getEntitiesOfClass(EnderDragon.class, player.getBoundingBox().inflate(500));
            dragons.forEach(dragon -> {
                EnhancedEndFightMain.LOGGER.info("Dragon Phase: {}", dragon.getPhaseManager().getCurrentPhase());
                player.sendSystemMessage(Component.literal("Dragon Phase: " + dragon.getPhaseManager().getCurrentPhase()));
                dragon.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0, false, false));
            });
        }
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
