package net.midget807.enhancedendfight.item;

import net.midget807.enhancedendfight.entity.OneShotTargetEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class OneShotTargetSpawnerItem extends SuccessItem {
    public OneShotTargetSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide) {
            if (!player.isShiftKeyDown()) {
                OneShotTargetEntity oneShotTargetEntity = new OneShotTargetEntity(level, player);
                level.addFreshEntity(oneShotTargetEntity);
            } else {
                List<OneShotTargetEntity> targetEntities = level.getEntitiesOfClass(OneShotTargetEntity.class, player.getBoundingBox().inflate(1.0F));
                targetEntities.forEach(Entity::kill);
            }
        }
        return super.use(level, player, usedHand);
    }
}
